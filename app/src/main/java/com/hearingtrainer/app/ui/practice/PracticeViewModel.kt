package com.hearingtrainer.app.ui.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hearingtrainer.app.audio.NotePlayer
import com.hearingtrainer.app.core.AnswerScorer
import com.hearingtrainer.app.core.DifficultyLevel
import com.hearingtrainer.app.core.MelodyGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/** Where the user is in one melody's play-then-answer cycle (see docs/SPEC.md's core loop). */
enum class PracticePhase { READY, PLAYING, ANSWERING, CORRECT, INCORRECT }

data class PracticeUiState(
    val phase: PracticePhase = PracticePhase.READY,
    val melodyLength: Int = 0,
    val answer: List<Int> = emptyList(),
    val perNoteCorrect: List<Boolean> = emptyList(),
)

/**
 * Drives one Practice screen. Owns the current melody and the user's in-progress answer;
 * everything it needs from `core/` and `audio/` is passed in, so this stays plain unidirectional
 * state (see CLAUDE.md: "MVVM, unidirectional data flow, one ViewModel per screen").
 *
 * Only Level 1 exists so far (see [DifficultyLevel]) — this always plays that level. Choosing a
 * level, and moving up the ladder, is follow-up work once there's somewhere to store progress.
 */
class PracticeViewModel(private val notePlayer: NotePlayer) : ViewModel() {

    private var currentMelody: List<Int> = emptyList()

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    /** Starts a new melody: generates it, shows it, then plays it. */
    fun onPlayClicked() {
        if (_uiState.value.phase == PracticePhase.PLAYING) return
        val melody = MelodyGenerator.generate(DifficultyLevel.level1(seed = Random.nextLong())).notes
        currentMelody = melody
        _uiState.value = PracticeUiState(phase = PracticePhase.PLAYING, melodyLength = melody.size)
        playMelody(melody)
    }

    /** Plays the same melody again, clearing whatever answer was in progress. */
    fun onReplayClicked() {
        if (currentMelody.isEmpty() || _uiState.value.phase == PracticePhase.PLAYING) return
        _uiState.value = _uiState.value.copy(
            phase = PracticePhase.PLAYING,
            answer = emptyList(),
            perNoteCorrect = emptyList(),
        )
        playMelody(currentMelody)
    }

    /** The user pressed a key on the on-screen keyboard while answering. */
    fun onKeyPressed(note: Int) {
        val state = _uiState.value
        if (state.phase != PracticePhase.ANSWERING) return

        notePlayer.play(note)
        val newAnswer = state.answer + note
        _uiState.value = state.copy(answer = newAnswer)

        if (newAnswer.size == currentMelody.size) {
            val result = AnswerScorer.score(currentMelody, newAnswer)
            _uiState.value = _uiState.value.copy(
                phase = if (result.allCorrect) PracticePhase.CORRECT else PracticePhase.INCORRECT,
                perNoteCorrect = result.perNoteCorrect,
            )
        }
    }

    /** Starts the next melody after seeing feedback. Just an alias of [onPlayClicked] for now —
     * v1 doesn't yet track a fixed-length session (see docs/SPEC.md's "after a fixed number of
     * melodies... the session ends with a score"), so "Next" and "Play" do the same thing. */
    fun onNextClicked() = onPlayClicked()

    private fun playMelody(melody: List<Int>) {
        viewModelScope.launch {
            for (note in melody) {
                notePlayer.play(note)
                delay(NOTE_INTERVAL_MS)
            }
            _uiState.value = _uiState.value.copy(phase = PracticePhase.ANSWERING)
        }
    }

    override fun onCleared() {
        notePlayer.release()
    }

    private companion object {
        const val NOTE_INTERVAL_MS = 650L
    }
}

/** [notePlayer] needs a Context to construct, so `viewModel()` needs this factory. */
class PracticeViewModelFactory(private val notePlayer: NotePlayer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PracticeViewModel(notePlayer) as T
    }
}
