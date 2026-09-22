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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearingtrainer.app.audio.SoundPoolNotePlayer
import com.hearingtrainer.app.ui.keyboard.PianoKeyboard

// Level 1's range from docs/SPEC.md: C major, C4-G4. Hardcoded alongside DifficultyLevel.level1
// until there's a real level picker.
private val LEVEL_1_OCTAVE_ROOT = 60
private val LEVEL_1_RANGE = (60..67).toSet()

@Composable
fun PracticeScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val notePlayer = remember { SoundPoolNotePlayer(context) }
    val viewModel: PracticeViewModel = viewModel(factory = remember { PracticeViewModelFactory(notePlayer) })
    val uiState by viewModel.uiState.collectAsState()

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
                Text(text = statusText(uiState.phase), style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                AnswerStrip(
                    length = uiState.melodyLength,
                    answeredCount = uiState.answer.size,
                    perNoteCorrect = uiState.perNoteCorrect,
                )
            }

            PianoKeyboard(
                octaveRootNote = LEVEL_1_OCTAVE_ROOT,
                highlightedNotes = LEVEL_1_RANGE,
                onKeyPressed = viewModel::onKeyPressed,
                modifier = Modifier.padding(vertical = 24.dp),
            )

            PracticeActions(
                phase = uiState.phase,
                onPlay = viewModel::onPlayClicked,
                onReplay = viewModel::onReplayClicked,
                onNext = viewModel::onNextClicked,
            )
        }
    }
}

@Composable
private fun AnswerStrip(length: Int, answeredCount: Int, perNoteCorrect: List<Boolean>) {
    if (length == 0) return
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

private fun statusText(phase: PracticePhase): String = when (phase) {
    PracticePhase.READY -> "Ready when you are"
    PracticePhase.PLAYING -> "Listen…"
    PracticePhase.ANSWERING -> "Your turn — play it back"
    PracticePhase.CORRECT -> "Correct!"
    PracticePhase.INCORRECT -> "Not quite"
}

@Composable
private fun PracticeActions(
    phase: PracticePhase,
    onPlay: () -> Unit,
    onReplay: () -> Unit,
    onNext: () -> Unit,
) {
    val buttonModifier = Modifier.fillMaxWidth().height(56.dp)
    when (phase) {
        PracticePhase.READY -> Button(onClick = onPlay, modifier = buttonModifier) {
            Text("Play")
        }
        PracticePhase.PLAYING -> Button(onClick = {}, enabled = false, modifier = buttonModifier) {
            Text("Listening…")
        }
        PracticePhase.ANSWERING -> Button(onClick = onReplay, modifier = buttonModifier) {
            Text("Replay")
        }
        PracticePhase.CORRECT, PracticePhase.INCORRECT -> Button(onClick = onNext, modifier = buttonModifier) {
            Text("Next")
        }
    }
}
