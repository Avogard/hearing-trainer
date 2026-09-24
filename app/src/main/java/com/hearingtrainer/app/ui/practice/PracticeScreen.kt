package com.hearingtrainer.app.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearingtrainer.app.core.Features
import com.hearingtrainer.app.ui.keyboard.PianoKeyboard

// Level 1's range from docs/SPEC.md: C major, C4-G4. Hardcoded alongside DifficultyLevel.level1
// until there's a real level picker.
private const val LEVEL_1_OCTAVE_ROOT = 60
private val LEVEL_1_RANGE = (60..67).toSet()

private val BUTTON_HEIGHT = 56.dp
private val BUTTON_GAP = 12.dp

@Composable
fun PracticeScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val viewModel: PracticeViewModel = viewModel(factory = remember { PracticeViewModelFactory(appContext) })
    val uiState by viewModel.uiState.collectAsState()

    // Runs when this screen enters the composition, and the onDispose block when it leaves
    // (going Home): the audio output only runs while the screen is actually visible.
    DisposableEffect(viewModel) {
        viewModel.onScreenShown()
        onDispose { viewModel.onScreenHidden() }
    }

    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onDone) {
            Text(text = "← Home")
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = statusText(uiState), style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                AnswerStrip(
                    length = uiState.melodyLength,
                    answeredCount = uiState.answer.size,
                    perNoteCorrect = uiState.perNoteCorrect,
                    canUndo = uiState.phase == PracticePhase.ANSWERING && uiState.answer.isNotEmpty() && !uiState.isPlaying,
                    onUndo = viewModel::onUndoClicked,
                )
            }

            PianoKeyboard(
                octaveRootNote = LEVEL_1_OCTAVE_ROOT,
                highlightedNotes = LEVEL_1_RANGE,
                onKeyPressed = viewModel::onKeyPressed,
                modifier = Modifier.padding(vertical = 24.dp),
            )

            PracticeActions(
                uiState = uiState,
                onPlay = viewModel::onPlayClicked,
                onReplay = viewModel::onReplayClicked,
                onHearAnswer = viewModel::onHearAnswerClicked,
                onCheck = viewModel::onCheckClicked,
                onNext = viewModel::onNextClicked,
            )
        }
    }
}

/**
 * One dot per note of the melody: empty until answered, filled while answering, green/red once
 * checked. The undo button sits at the end of the row; its space is always reserved so the dots
 * don't shift when it appears.
 */
@Composable
private fun AnswerStrip(
    length: Int,
    answeredCount: Int,
    perNoteCorrect: List<Boolean>,
    canUndo: Boolean,
    onUndo: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(48.dp)) {
        Spacer(modifier = Modifier.width(48.dp)) // balances the undo slot so the dots stay centered
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            for (i in 0 until length) {
                val color = when {
                    i < perNoteCorrect.size -> {
                        if (perNoteCorrect[i]) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    }
                    i < answeredCount -> MaterialTheme.colorScheme.onSurfaceVariant
                    else -> MaterialTheme.colorScheme.outlineVariant
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color),
                )
            }
        }
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            if (canUndo) {
                IconButton(onClick = onUndo) {
                    Text(text = "⌫", fontSize = 22.sp)
                }
            }
        }
    }
}

private fun statusText(state: PracticeUiState): String = when {
    state.phase == PracticePhase.CHECKED -> if (state.allCorrect) "Correct!" else "Not quite"
    state.playback == Playback.MELODY -> "Listen…"
    state.playback == Playback.ANSWER -> "Your answer…"
    state.phase == PracticePhase.READY -> "Ready when you are"
    state.isAnswerComplete -> "Hear it back, or check it"
    else -> "Your turn — play it back"
}

/**
 * The buttons under the keyboard. Always the same height (two rows) so the keyboard never jumps:
 * READY shows just "Play melody"; after that the top row is "Replay" / "My answer" and the big
 * button is "Check" (while answering) or "Next" (once checked).
 */
@Composable
private fun PracticeActions(
    uiState: PracticeUiState,
    onPlay: () -> Unit,
    onReplay: () -> Unit,
    onHearAnswer: () -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (uiState.phase == PracticePhase.READY) {
            Spacer(modifier = Modifier.height(BUTTON_HEIGHT))
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                PlayButton(
                    text = "Replay",
                    enabled = !uiState.isPlaying,
                    onClick = onReplay,
                    modifier = Modifier.weight(1f),
                )
                if (Features.HEAR_MY_ANSWER) {
                    Spacer(modifier = Modifier.width(BUTTON_GAP))
                    PlayButton(
                        text = "My answer",
                        enabled = !uiState.isPlaying && uiState.answer.isNotEmpty(),
                        onClick = onHearAnswer,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(BUTTON_GAP))

        val primaryModifier = Modifier.fillMaxWidth().height(BUTTON_HEIGHT)
        when (uiState.phase) {
            PracticePhase.READY -> Button(onClick = onPlay, modifier = primaryModifier) {
                Text(text = "Play melody")
            }
            PracticePhase.ANSWERING -> Button(
                onClick = onCheck,
                enabled = uiState.isAnswerComplete && !uiState.isPlaying,
                modifier = primaryModifier,
            ) {
                Text(text = "Check")
            }
            PracticePhase.CHECKED -> Button(onClick = onNext, modifier = primaryModifier) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
private fun PlayButton(text: String, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, enabled = enabled, modifier = modifier.height(BUTTON_HEIGHT)) {
        Text(text = text)
    }
}
