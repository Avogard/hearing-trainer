package com.hearingtrainer.app.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearingtrainer.app.core.AnswerMode
import com.hearingtrainer.app.core.Features
import com.hearingtrainer.app.ui.keyboard.PianoKeyboard
import com.hearingtrainer.app.ui.theme.DotAmber
import com.hearingtrainer.app.ui.theme.DotGreen
import com.hearingtrainer.app.ui.theme.DotRed
import com.hearingtrainer.app.ui.theme.KeyLit

// Level 1's range from docs/SPEC.md: C major, C4-G4. Hardcoded alongside DifficultyLevel.level1
// until there's a real level picker.
private const val LEVEL_1_OCTAVE_ROOT = 60
private val LEVEL_1_RANGE = (60..67).toSet()

private val BUTTON_HEIGHT = 56.dp
private val TOGGLE_HEIGHT = 48.dp
private val BUTTON_GAP = 12.dp
private val SCREEN_GUTTER = 24.dp

/** The keyboard overhangs the screen gutter so seven white keys stay at least 48 dp wide on a 360 dp phone. */
private val KEYBOARD_GUTTER = 8.dp

/**
 * The Practice screen, top to bottom: Home and the Scored / Free play toggle on one row, the
 * status line, the answer strip, the feedback lines (they take whatever height is left), the
 * keyboard, and the buttons. The keyboard and buttons sit at the bottom, under the thumb.
 */
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

    val gutter = Modifier.fillMaxWidth().padding(horizontal = SCREEN_GUTTER)
    Column(modifier = modifier.fillMaxSize().padding(vertical = SCREEN_GUTTER)) {
        Row(modifier = gutter, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onDone) {
                Text(text = "← Home")
            }
            if (Features.SCORED_ANSWER_MODE) {
                Spacer(modifier = Modifier.width(BUTTON_GAP))
                ModeToggle(
                    mode = uiState.mode,
                    enabled = uiState.modeToggleEnabled,
                    onSelected = viewModel::onModeSelected,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = gutter, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = statusText(uiState), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            AnswerStrip(
                dots = uiState.dots,
                openPosition = uiState.openPosition,
                canUndo = uiState.mode == AnswerMode.FREE &&
                    uiState.phase == PracticePhase.ANSWERING &&
                    uiState.answer.isNotEmpty() &&
                    !uiState.isPlaying,
                onUndo = viewModel::onUndoClicked,
            )
        }

        FeedbackLines(
            lines = uiState.feedbackLines,
            modifier = Modifier.weight(1f).then(gutter).padding(vertical = 12.dp),
        )

        PianoKeyboard(
            octaveRootNote = LEVEL_1_OCTAVE_ROOT,
            highlightedNotes = LEVEL_1_RANGE,
            litNote = uiState.revealedNote,
            onKeyPressed = viewModel::onKeyPressed,
            modifier = Modifier.padding(start = KEYBOARD_GUTTER, end = KEYBOARD_GUTTER, bottom = 16.dp),
        )

        PracticeActions(
            modifier = gutter,
            uiState = uiState,
            onPlay = viewModel::onPlayClicked,
            onReplay = viewModel::onReplayClicked,
            onHearAnswer = viewModel::onHearAnswerClicked,
            onCheck = viewModel::onCheckClicked,
            onNext = viewModel::onNextClicked,
        )
    }
}

/**
 * Scored / Free play, as one segmented control (Material's two-option switch, each half a full
 * button). Disabled while a melody is being answered: the mode applies from the next melody.
 */
@Composable
private fun ModeToggle(
    mode: AnswerMode,
    enabled: Boolean,
    onSelected: (AnswerMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.height(TOGGLE_HEIGHT)) {
        SegmentedButton(
            selected = mode == AnswerMode.SCORED,
            onClick = { onSelected(AnswerMode.SCORED) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            enabled = enabled,
        ) {
            Text(text = "Scored")
        }
        SegmentedButton(
            selected = mode == AnswerMode.FREE,
            onClick = { onSelected(AnswerMode.FREE) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            enabled = enabled,
        ) {
            Text(text = "Free play")
        }
    }
}

/**
 * One dot per note of the melody, coloured by [DotState]: green right, amber found after wrong
 * presses, red wrong or revealed; the dot at [openPosition] (scored: the one the next press
 * answers) is outlined whatever its colour. The undo button (free play only) sits at the end of
 * the row; its space is always reserved so the dots don't shift when it appears.
 */
@Composable
private fun AnswerStrip(dots: List<DotState>, openPosition: Int?, canUndo: Boolean, onUndo: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(48.dp)) {
        Spacer(modifier = Modifier.width(48.dp)) // balances the undo slot so the dots stay centered
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            dots.forEachIndexed { index, dot ->
                Dot(dot, outlined = index == openPosition)
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

@Composable
private fun Dot(state: DotState, outlined: Boolean) {
    val fill = when (state) {
        DotState.EMPTY -> MaterialTheme.colorScheme.outlineVariant
        DotState.FILLED -> MaterialTheme.colorScheme.onSurfaceVariant
        DotState.CORRECT -> DotGreen
        DotState.FOUND -> DotAmber
        DotState.WRONG -> DotRed
    }
    val outline = if (outlined) MaterialTheme.colorScheme.onSurface else Color.Transparent
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(fill)
            .border(width = 2.dp, color = outline, shape = CircleShape),
    )
}

/** "3rd note: you played E4, it was F4", one per line, scrollable if a long melody needs it. */
@Composable
private fun FeedbackLines(lines: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (line in lines) {
            Text(text = line, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

/** The result stays up once a melody is done, even while it plays once more; before that, what's sounding wins. */
private fun statusText(state: PracticeUiState): String = when {
    state.phase == PracticePhase.CHECKED -> when {
        state.mode == AnswerMode.SCORED -> "${state.firstTryCount} of ${state.melodyLength} first try"
        state.allCorrect -> "Correct!"
        else -> "Not quite"
    }
    state.playback == Playback.ANSWER -> "Your answer…"
    state.isPlaying -> "Listen…"
    state.phase == PracticePhase.READY -> "Ready when you are"
    state.mode == AnswerMode.SCORED -> when {
        state.wrongPressesAtPosition > 0 -> "Try again — note ${state.currentPosition} of ${state.melodyLength}"
        else -> "Your turn — note ${state.currentPosition} of ${state.melodyLength}"
    }
    state.isAnswerComplete -> "Hear it back, or check it"
    else -> "Your turn — play it back"
}

/**
 * The buttons under the keyboard. Always the same height (two rows) so the keyboard never jumps.
 * READY shows just "Play melody". Scored: "Replay (n left)" on top while a replay is still on
 * offer (plain, disabled "Replay" once the first press has locked it), and the big button is
 * empty while answering (the melody finishes itself) or "Next" once done. Free play: "Replay" /
 * "My answer" on top, "Check" while answering, "Next" once checked.
 */
@Composable
private fun PracticeActions(
    uiState: PracticeUiState,
    onPlay: () -> Unit,
    onReplay: () -> Unit,
    onHearAnswer: () -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scored = uiState.mode == AnswerMode.SCORED
    Column(modifier = modifier) {
        if (uiState.phase == PracticePhase.READY) {
            Spacer(modifier = Modifier.height(BUTTON_HEIGHT))
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                val showCount = scored && uiState.phase == PracticePhase.ANSWERING &&
                    (uiState.replayAllowed || uiState.replaysLeft == 0)
                val replayLabel = if (showCount) "Replay (${uiState.replaysLeft} left)" else "Replay"
                PlayButton(
                    text = replayLabel,
                    enabled = uiState.replayEnabled,
                    onClick = onReplay,
                    modifier = Modifier.weight(1f),
                )
                if (!scored && Features.HEAR_MY_ANSWER) {
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
            PracticePhase.ANSWERING -> if (scored) {
                Spacer(modifier = Modifier.height(BUTTON_HEIGHT))
            } else {
                Button(
                    onClick = onCheck,
                    enabled = uiState.isAnswerComplete && !uiState.isPlaying,
                    modifier = primaryModifier,
                ) {
                    Text(text = "Check")
                }
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
