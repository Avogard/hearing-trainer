package com.hearingtrainer.app.ui.practice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hearingtrainer.app.audio.MixerNotePlayer
import com.hearingtrainer.app.audio.NotePlayer
import com.hearingtrainer.app.core.AnswerMode
import com.hearingtrainer.app.core.AnswerOutcome
import com.hearingtrainer.app.core.AnswerScorer
import com.hearingtrainer.app.core.AttemptLog
import com.hearingtrainer.app.core.AttemptRecord
import com.hearingtrainer.app.core.Clock
import com.hearingtrainer.app.core.Config
import com.hearingtrainer.app.core.DifficultyLevel
import com.hearingtrainer.app.core.Features
import com.hearingtrainer.app.core.MelodyGenerator
import com.hearingtrainer.app.core.MelodyTiming
import com.hearingtrainer.app.core.PositionOutcome
import com.hearingtrainer.app.core.PressResult
import com.hearingtrainer.app.core.ScoredAnswer
import com.hearingtrainer.app.core.SettingsStore
import com.hearingtrainer.app.core.TimedNote
import com.hearingtrainer.app.core.TonalContext
import com.hearingtrainer.app.data.FileAttemptLog
import com.hearingtrainer.app.data.RealClock
import com.hearingtrainer.app.data.SharedPreferencesSettingsStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

/** Where the user is in one melody's play-then-answer cycle (see docs/SPEC.md's core loop). */
enum class PracticePhase { READY, ANSWERING, CHECKED }

/** What, if anything, is sounding right now. Key presses are ignored while something plays. */
enum class Playback { NOTHING, MELODY, ANSWER, REVEAL }

/** How one dot of the answer strip is drawn (docs/SPEC.md, "Answer modes"). */
enum class DotState {
    /** Not reached yet (scored: also the open position while it has no wrong press). */
    EMPTY,
    /** Free play: answered, not checked yet. */
    FILLED,
    /** Right: at the first try (scored) or at Check (free play). */
    CORRECT,
    /** Scored: right after one or more wrong presses. */
    FOUND,
    /** Wrong: a wrong press at the open position, a revealed note (scored), or wrong at Check (free play). */
    WRONG,
}

data class PracticeUiState(
    val mode: AnswerMode = defaultMode(),
    val phase: PracticePhase = PracticePhase.READY,
    val playback: Playback = Playback.NOTHING,
    /** One entry per note of the melody; the only source for the strip's colours in both modes. */
    val dots: List<DotState> = emptyList(),
    /** Scored, while answering: index of the position the next press answers (outlined on the strip). */
    val openPosition: Int? = null,
    /** Whether the rules allow a replay right now; the button also needs nothing to be playing. */
    val replayAllowed: Boolean = false,
    // --- free play ---
    val answer: List<Int> = emptyList(),
    val allCorrect: Boolean = false,
    // --- scored ---
    val replaysLeft: Int = 0,
    val wrongPressesAtPosition: Int = 0,
    /** The key lit up during a reveal, if any. */
    val revealedNote: Int? = null,
    val firstTryCount: Int = 0,
    /** After a scored melody: one line per note that wasn't a first try. */
    val feedbackLines: List<String> = emptyList(),
) {
    val melodyLength: Int get() = dots.size
    val isPlaying: Boolean get() = playback != Playback.NOTHING
    val isAnswerComplete: Boolean get() = melodyLength > 0 && answer.size == melodyLength
    val replayEnabled: Boolean get() = replayAllowed && !isPlaying

    /** The scored position being answered, 1-based, for the status line; 0 outside answering. */
    val currentPosition: Int get() = (openPosition ?: -1) + 1

    /** The mode toggle works between melodies only, never while one is being answered or sounding. */
    val modeToggleEnabled: Boolean get() = phase != PracticePhase.ANSWERING && !isPlaying

    companion object {
        fun defaultMode(): AnswerMode = if (Features.SCORED_ANSWER_MODE) AnswerMode.SCORED else AnswerMode.FREE
    }
}

/**
 * Drives one Practice screen. Owns the current melody and the answer in progress; everything it
 * needs from `core/`, `audio/` and `data/` is passed in, so this stays plain unidirectional state
 * (CLAUDE.md: "MVVM, unidirectional data flow, one ViewModel per screen").
 *
 * Two answer modes (docs/SPEC.md, "Answer modes"), switched between melodies with [onModeSelected]:
 *  - Scored (default): Play -> cadence, gap, melody -> every key press is an answer, judged by a
 *    [ScoredAnswer]; limited replays before the first press; too many wrong presses reveal the
 *    note; the melody finishes itself and, if anything wasn't a first try, plays once more.
 *  - Free play: Play -> cadence, gap, melody -> keys fill the strip, with unlimited Replay,
 *    My answer and undo -> Check scores it with [AnswerScorer] -> Next.
 * Both modes log a record per finished melody. Only Level 1 exists so far (see [DifficultyLevel]);
 * tempo and length come from settings.
 */
class PracticeViewModel(
    private val notePlayer: NotePlayer,
    private val settingsStore: SettingsStore,
    private val attemptLog: AttemptLog,
    private val clock: Clock,
) : ViewModel() {

    private var melody: List<Int> = emptyList()
    private var keyRoot: Int = 0
    private var cadence: List<List<Int>> = emptyList()
    private var tempoBpm: Int = Config.DEFAULT_TEMPO_BPM
    private var scored: ScoredAnswer? = null
    private var freeReplays: Int = 0

    /** The one job for anything timed: a playback wait, or the reveal-then-replay tail. */
    private var playbackJob: Job? = null

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    /** The Scored / Free play toggle. Takes effect at the next Play; ignored mid-melody. */
    fun onModeSelected(mode: AnswerMode) {
        val state = _uiState.value
        if (!Features.SCORED_ANSWER_MODE || mode == state.mode || !state.modeToggleEnabled) return
        melody = emptyList()
        scored = null
        _uiState.value = PracticeUiState(mode = mode)
    }

    /** Starts a new melody with the current settings: generates it, then plays it (with its cadence). */
    fun onPlayClicked() {
        val settings = settingsStore.load()
        tempoBpm = settings.tempoBpm
        val spec = DifficultyLevel.level1(seed = Random.nextLong(), length = settings.melodyLength)
        melody = MelodyGenerator.generate(spec).notes
        keyRoot = spec.rootNote
        cadence = if (Features.CADENCE_BEFORE_MELODY) {
            TonalContext.cadenceChords(rootMidi = spec.rootNote, melodyLowest = spec.lowestNote)
        } else {
            emptyList()
        }
        freeReplays = 0
        val mode = _uiState.value.mode
        val answer = if (mode == AnswerMode.SCORED) {
            ScoredAnswer(melody, Config.MAX_REPLAYS_BEFORE_FIRST_PRESS, Config.MAX_WRONG_PRESSES_PER_NOTE)
        } else {
            null
        }
        scored = answer
        _uiState.value = PracticeUiState(
            mode = mode,
            phase = PracticePhase.ANSWERING,
            dots = if (answer != null) scoredDots(answer) else freeDots(emptyList(), null),
            openPosition = answer?.position,
            replayAllowed = answer?.canReplay() ?: true,
            replaysLeft = answer?.replaysLeft ?: 0,
        )
        startPlayback(MelodyTiming.scheduleWithCadence(cadence, melody, tempoBpm), Playback.MELODY)
    }

    /**
     * Plays the melody again (without the cadence). Free play: any time after Play. Scored: only
     * before the first press and only [Config.MAX_REPLAYS_BEFORE_FIRST_PRESS] times.
     */
    fun onReplayClicked() {
        val state = _uiState.value
        if (melody.isEmpty() || state.isPlaying || state.phase == PracticePhase.READY) return
        val answer = scored
        if (state.mode == AnswerMode.SCORED) {
            if (answer == null || state.phase != PracticePhase.ANSWERING || !answer.canReplay()) return
            answer.noteReplay()
            _uiState.update { it.copy(replayAllowed = answer.canReplay(), replaysLeft = answer.replaysLeft) }
        } else if (state.phase == PracticePhase.ANSWERING) {
            freeReplays++
        }
        startPlayback(MelodyTiming.schedule(melody, tempoBpm), Playback.MELODY)
    }

    /** Free play: plays back what the user has entered so far, at the same tempo as the melody. */
    fun onHearAnswerClicked() {
        val state = _uiState.value
        if (state.mode != AnswerMode.FREE || state.answer.isEmpty() || state.isPlaying) return
        startPlayback(MelodyTiming.schedule(state.answer, tempoBpm), Playback.ANSWER)
    }

    /**
     * A key on the on-screen keyboard was tapped. Scored mode while answering: the press is the
     * answer for the current position (it sounds too, unless [Features.SILENT_SCORED_KEYBOARD]).
     * Otherwise the key just sounds — the keyboard is also a keyboard — and in free play it is
     * appended to the answer while the answer isn't full.
     */
    fun onKeyPressed(note: Int) {
        val state = _uiState.value
        if (state.isPlaying) return
        val answer = scored
        if (state.mode == AnswerMode.SCORED && answer != null && state.phase == PracticePhase.ANSWERING) {
            if (!Features.SILENT_SCORED_KEYBOARD) notePlayer.play(note)
            afterScoredPress(answer, answer.press(note, clock.elapsedMillis()))
            return
        }
        notePlayer.play(note)
        if (state.mode == AnswerMode.FREE && state.phase == PracticePhase.ANSWERING && !state.isAnswerComplete) {
            val newAnswer = state.answer + note
            _uiState.update { it.copy(answer = newAnswer, dots = freeDots(newAnswer, null)) }
        }
    }

    /** Free play: removes the last note of the answer. */
    fun onUndoClicked() {
        val state = _uiState.value
        if (state.mode != AnswerMode.FREE || state.phase != PracticePhase.ANSWERING || state.isPlaying || state.answer.isEmpty()) return
        val newAnswer = state.answer.dropLast(1)
        _uiState.update { it.copy(answer = newAnswer, dots = freeDots(newAnswer, null)) }
    }

    /** Free play: scores the completed answer and shows per-note feedback. */
    fun onCheckClicked() {
        val state = _uiState.value
        if (state.mode != AnswerMode.FREE || state.phase != PracticePhase.ANSWERING || !state.isAnswerComplete || state.isPlaying) return
        val result = AnswerScorer.score(melody, state.answer)
        _uiState.update {
            it.copy(
                phase = PracticePhase.CHECKED,
                allCorrect = result.allCorrect,
                dots = freeDots(state.answer, result.perNoteCorrect),
            )
        }
        log(
            AttemptRecord.free(
                timestampMillis = clock.nowMillis(),
                keyRoot = keyRoot,
                melody = melody,
                answer = state.answer,
                tempoBpm = tempoBpm,
                cadencePlayed = cadence.isNotEmpty(),
                replaysUsed = freeReplays,
            )
        )
        if (!result.allCorrect && Features.REPLAY_MELODY_AFTER_WRONG_ANSWER) {
            startPlayback(MelodyTiming.schedule(melody, tempoBpm), Playback.MELODY)
        }
    }

    /** Starts the next melody after seeing feedback. */
    fun onNextClicked() = onPlayClicked()

    /** The Practice screen became visible: make sure the audio output is running. */
    fun onScreenShown() {
        notePlayer.setActive(true)
        // Whatever was sounding when the screen went away is gone; if a scored answer is open,
        // its keys are available again from now.
        scored?.takeIf { _uiState.value.phase == PracticePhase.ANSWERING }?.inputOpened(clock.elapsedMillis())
    }

    /** The Practice screen went away (Home, another app): silence and idle the audio output. */
    fun onScreenHidden() {
        stopPlayback()
        notePlayer.setActive(false)
    }

    // --- scored mode -------------------------------------------------------------------------

    private fun afterScoredPress(answer: ScoredAnswer, result: PressResult) {
        _uiState.update {
            it.copy(
                dots = scoredDots(answer),
                openPosition = if (answer.isFinished) null else answer.position,
                replayAllowed = false,
                wrongPressesAtPosition = answer.wrongPressesAtPosition,
            )
        }
        val outcome = if (result.finished) answer.result() else null
        if (outcome != null) finishScored(answer, outcome)
        val reveal = result as? PressResult.Revealed
        val replayMelody = outcome != null && !outcome.clean && Features.REPLAY_MELODY_AFTER_WRONG_ANSWER
        if (reveal != null || replayMelody) playAudioTail(reveal, replayMelody)
    }

    private fun finishScored(answer: ScoredAnswer, outcome: AnswerOutcome) {
        _uiState.update {
            it.copy(
                phase = PracticePhase.CHECKED,
                firstTryCount = outcome.firstTryCount,
                feedbackLines = outcome.feedbackLines(),
            )
        }
        log(
            AttemptRecord.scored(
                timestampMillis = clock.nowMillis(),
                keyRoot = keyRoot,
                melody = melody,
                tempoBpm = tempoBpm,
                cadencePlayed = cadence.isNotEmpty(),
                replaysUsed = answer.replaysUsed,
                outcome = outcome,
                silentKeyboard = Features.SILENT_SCORED_KEYBOARD,
            )
        )
    }

    /**
     * The sounds that follow a scored press, as one job so a later press or hiding the screen
     * cancels all of it: a reveal (the pressed note has already sounded; after a short delay the
     * correct note sounds with its key lit), then — after the last note of a melody that wasn't
     * all first try — a beat of silence and the melody once more. Keys are ignored throughout.
     */
    private fun playAudioTail(reveal: PressResult.Revealed?, replayMelody: Boolean) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            try {
                if (reveal != null) {
                    _uiState.update { it.copy(playback = Playback.REVEAL) }
                    delay(Config.REVEAL_DELAY_MILLIS)
                    notePlayer.play(reveal.correctNote)
                    _uiState.update { it.copy(revealedNote = reveal.correctNote) }
                    delay(Config.REVEAL_HIGHLIGHT_MILLIS)
                    _uiState.update { it.copy(revealedNote = null) }
                }
                if (replayMelody) {
                    _uiState.update { it.copy(playback = Playback.MELODY) }
                    delay(millis(Config.REPLAY_AFTER_FINISH_GAP_BEATS * MelodyTiming.beatSeconds(tempoBpm)))
                    val sequence = MelodyTiming.schedule(melody, tempoBpm)
                    notePlayer.playSequence(sequence)
                    delay(millis(MelodyTiming.totalSeconds(sequence, tempoBpm)))
                }
            } finally {
                _uiState.update { it.copy(playback = Playback.NOTHING, revealedNote = null) }
                inputOpenedIfAnswering()
            }
        }
    }

    private fun scoredDots(answer: ScoredAnswer): List<DotState> {
        val resolved = answer.resolvedOutcomes
        return List(melody.size) { i ->
            when {
                i < resolved.size -> resolved[i].toDot()
                i == answer.position && answer.wrongPressesAtPosition > 0 -> DotState.WRONG
                else -> DotState.EMPTY
            }
        }
    }

    private fun PositionOutcome.toDot(): DotState = when (this) {
        PositionOutcome.FIRST_TRY, PositionOutcome.CORRECT -> DotState.CORRECT
        PositionOutcome.FOUND -> DotState.FOUND
        PositionOutcome.REVEALED, PositionOutcome.WRONG -> DotState.WRONG
    }

    /** A scored answer that is still open gets to know the keys are available again now. */
    private fun inputOpenedIfAnswering() {
        val answer = scored ?: return
        if (_uiState.value.phase == PracticePhase.ANSWERING && !answer.isFinished) answer.inputOpened(clock.elapsedMillis())
    }

    // --- free play ---------------------------------------------------------------------------

    /** Dots for a free-play answer: filled as entered, coloured once [perNoteCorrect] is known. */
    private fun freeDots(answer: List<Int>, perNoteCorrect: List<Boolean>?): List<DotState> = List(melody.size) { i ->
        when {
            perNoteCorrect != null -> if (perNoteCorrect[i]) DotState.CORRECT else DotState.WRONG
            i < answer.size -> DotState.FILLED
            else -> DotState.EMPTY
        }
    }

    // --- shared ------------------------------------------------------------------------------

    /**
     * Hands [sequence] to the engine, which times it sample-accurately, and flips the UI back once
     * it must be over. A scored answer that is still open is told the keys are available then.
     */
    private fun startPlayback(sequence: List<TimedNote>, what: Playback) {
        playbackJob?.cancel()
        _uiState.update { it.copy(playback = what, revealedNote = null) }
        notePlayer.playSequence(sequence)
        playbackJob = viewModelScope.launch {
            delay(millis(MelodyTiming.totalSeconds(sequence, tempoBpm)))
            _uiState.update { it.copy(playback = Playback.NOTHING) }
            inputOpenedIfAnswering()
        }
    }

    /** Cancels whatever is timed and clears every bit of state that only makes sense while it runs. */
    private fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null
        _uiState.update { it.copy(playback = Playback.NOTHING, revealedNote = null) }
    }

    private fun log(record: AttemptRecord) {
        if (Features.LOG_ATTEMPTS) attemptLog.append(record)
    }

    private fun millis(seconds: Double): Long = (seconds * 1000).toLong()

    override fun onCleared() {
        playbackJob?.cancel()
        notePlayer.release()
    }
}

/**
 * The dependencies need a Context to construct, so `viewModel()` needs this factory. The
 * ViewModel — and with it the audio engine — lives as long as the Activity, so navigating
 * Home <-> Practice doesn't rebuild the engine each time.
 */
class PracticeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        return PracticeViewModel(
            notePlayer = MixerNotePlayer(appContext),
            settingsStore = SharedPreferencesSettingsStore(appContext),
            attemptLog = FileAttemptLog(appContext),
            clock = RealClock,
        ) as T
    }
}
