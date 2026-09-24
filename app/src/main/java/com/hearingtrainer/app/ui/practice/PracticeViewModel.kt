package com.hearingtrainer.app.ui.practice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hearingtrainer.app.audio.MixerNotePlayer
import com.hearingtrainer.app.audio.NotePlayer
import com.hearingtrainer.app.core.AnswerScorer
import com.hearingtrainer.app.core.Config
import com.hearingtrainer.app.core.DifficultyLevel
import com.hearingtrainer.app.core.Features
import com.hearingtrainer.app.core.MelodyGenerator
import com.hearingtrainer.app.core.MelodyTiming
import com.hearingtrainer.app.core.SettingsStore
import com.hearingtrainer.app.data.SharedPreferencesSettingsStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/** Where the user is in one melody's play-then-answer cycle (see docs/SPEC.md's core loop). */
enum class PracticePhase { READY, ANSWERING, CHECKED }

/** What, if anything, is sounding right now. Key presses are ignored while something plays. */
enum class Playback { NOTHING, MELODY, ANSWER }

data class PracticeUiState(
    val phase: PracticePhase = PracticePhase.READY,
    val playback: Playback = Playback.NOTHING,
    val melodyLength: Int = 0,
    val answer: List<Int> = emptyList(),
    val perNoteCorrect: List<Boolean> = emptyList(),
    val allCorrect: Boolean = false,
) {
    val isPlaying: Boolean get() = playback != Playback.NOTHING
    val isAnswerComplete: Boolean get() = melodyLength > 0 && answer.size == melodyLength
}

/**
 * Drives one Practice screen. Owns the current melody and the user's in-progress answer;
 * everything it needs from `core/`, `audio/` and `data/` is passed in, so this stays plain
 * unidirectional state (see CLAUDE.md: "MVVM, unidirectional data flow, one ViewModel per screen").
 *
 * The flow: Play -> the melody sounds -> the user taps keys (each one sounds and fills a dot) ->
 * they may hear the melody again, hear their own answer, or undo notes -> Check scores it ->
 * Next. Nothing is scored until the user asks, so they can always listen before committing.
 *
 * Only Level 1 exists so far (see [DifficultyLevel]); tempo and length come from settings.
 */
class PracticeViewModel(
    private val notePlayer: NotePlayer,
    private val settingsStore: SettingsStore,
) : ViewModel() {

    private var melody: List<Int> = emptyList()
    private var tempoBpm: Int = Config.DEFAULT_TEMPO_BPM
    private var playbackJob: Job? = null

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    /** Starts a new melody with the current settings: generates it, then plays it. */
    fun onPlayClicked() {
        val settings = settingsStore.load()
        tempoBpm = settings.tempoBpm
        melody = MelodyGenerator
            .generate(DifficultyLevel.level1(seed = Random.nextLong(), length = settings.melodyLength))
            .notes
        _uiState.value = PracticeUiState(phase = PracticePhase.ANSWERING, melodyLength = melody.size)
        startPlayback(melody, Playback.MELODY)
    }

    /** Plays the melody again. The answer in progress is kept — there's Undo for changing it. */
    fun onReplayClicked() {
        if (melody.isEmpty() || _uiState.value.isPlaying) return
        startPlayback(melody, Playback.MELODY)
    }

    /** Plays back what the user has entered so far, at the same tempo as the melody. */
    fun onHearAnswerClicked() {
        val state = _uiState.value
        if (state.answer.isEmpty() || state.isPlaying) return
        startPlayback(state.answer, Playback.ANSWER)
    }

    /**
     * A key on the on-screen keyboard was tapped. It always sounds (the keyboard is also just a
     * keyboard); it is appended to the answer only while answering and the answer isn't full.
     */
    fun onKeyPressed(note: Int) {
        val state = _uiState.value
        if (state.isPlaying) return
        notePlayer.play(note)
        if (state.phase == PracticePhase.ANSWERING && !state.isAnswerComplete) {
            _uiState.value = state.copy(answer = state.answer + note)
        }
    }

    /** Removes the last note of the answer. */
    fun onUndoClicked() {
        val state = _uiState.value
        if (state.phase != PracticePhase.ANSWERING || state.isPlaying || state.answer.isEmpty()) return
        _uiState.value = state.copy(answer = state.answer.dropLast(1))
    }

    /** Scores the completed answer and shows per-note feedback. */
    fun onCheckClicked() {
        val state = _uiState.value
        if (state.phase != PracticePhase.ANSWERING || !state.isAnswerComplete || state.isPlaying) return
        val result = AnswerScorer.score(melody, state.answer)
        _uiState.value = state.copy(
            phase = PracticePhase.CHECKED,
            perNoteCorrect = result.perNoteCorrect,
            allCorrect = result.allCorrect,
        )
        if (!result.allCorrect && Features.REPLAY_MELODY_AFTER_WRONG_ANSWER) {
            startPlayback(melody, Playback.MELODY)
        }
    }

    /** Starts the next melody after seeing feedback. */
    fun onNextClicked() = onPlayClicked()

    /** The Practice screen became visible: make sure the audio output is running. */
    fun onScreenShown() {
        notePlayer.setActive(true)
    }

    /** The Practice screen went away (Home, another app): silence and idle the audio output. */
    fun onScreenHidden() {
        playbackJob?.cancel()
        _uiState.value = _uiState.value.copy(playback = Playback.NOTHING)
        notePlayer.setActive(false)
    }

    private fun startPlayback(notes: List<Int>, what: Playback) {
        playbackJob?.cancel()
        _uiState.value = _uiState.value.copy(playback = what)
        // The engine times the notes itself, sample-accurately; this coroutine only needs to know
        // roughly when it's over to hand control back to the user.
        notePlayer.playSequence(MelodyTiming.schedule(notes, tempoBpm))
        playbackJob = viewModelScope.launch {
            delay((MelodyTiming.totalSeconds(notes.size, tempoBpm) * 1000).toLong())
            _uiState.value = _uiState.value.copy(playback = Playback.NOTHING)
        }
    }

    override fun onCleared() {
        playbackJob?.cancel()
        notePlayer.release()
    }
}

/**
 * Both dependencies need a Context to construct, so `viewModel()` needs this factory. The
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
        ) as T
    }
}
