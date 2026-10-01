package com.hearingtrainer.app.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearingtrainer.app.core.AnswerMode
import com.hearingtrainer.app.core.Features
import com.hearingtrainer.app.core.MidiNote
import com.hearingtrainer.app.ui.components.BackIcon
import com.hearingtrainer.app.ui.components.ButtonHeight
import com.hearingtrainer.app.ui.components.ButtonSlot
import com.hearingtrainer.app.ui.components.CheckIcon
import com.hearingtrainer.app.ui.components.CrossIcon
import com.hearingtrainer.app.ui.components.IconSquareButton
import com.hearingtrainer.app.ui.components.PrimaryButton
import com.hearingtrainer.app.ui.components.ReplayIcon
import com.hearingtrainer.app.ui.components.SecondaryButton
import com.hearingtrainer.app.ui.components.UndoIcon
import com.hearingtrainer.app.ui.keyboard.PianoKeyboard
import com.hearingtrainer.app.ui.theme.HearingTheme

// Level 1's range from docs/SPEC.md: C major, C4-G4. Hardcoded alongside DifficultyLevel.level1
// until there's a real level picker.
private const val LEVEL_1_OCTAVE_ROOT = 60
private val LEVEL_1_RANGE = (60..67).toSet()

private val BUTTON_GAP = 12.dp
private val SCREEN_GUTTER = 24.dp
private val DOT_SIZE = 44.dp
private val DOT_GAP = 12.dp

/** The keyboard overhangs the screen gutter so seven white keys stay at least 48 dp wide on a 360 dp phone. */
private val KEYBOARD_GUTTER = 8.dp

/**
 * The Practice screen, top to bottom: the Back button and the Scored / Free play toggle, the
 * status (a heading and a line under it), the answer strip, the feedback area (it takes whatever
 * height is left), the keyboard, and two rows of buttons. The keyboard and buttons sit at the
 * bottom, under the thumb.
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
    Column(modifier = modifier.fillMaxSize().padding(top = 12.dp, bottom = SCREEN_GUTTER)) {
        Row(
            modifier = gutter,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BUTTON_GAP),
        ) {
            IconSquareButton(onClick = onDone, contentDescription = "Back to Home") { BackIcon() }
            if (Features.SCORED_ANSWER_MODE) {
                ModeToggle(
                    mode = uiState.mode,
                    enabled = uiState.modeToggleEnabled,
                    onSelected = viewModel::onModeSelected,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        val status = statusOf(uiState)
        Column(modifier = gutter.padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = status.title, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Text(
                text = status.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp),
            )
            AnswerStrip(
                dots = uiState.dots,
                openPosition = uiState.openPosition,
                canUndo = uiState.mode == AnswerMode.FREE &&
                    uiState.phase == PracticePhase.ANSWERING &&
                    uiState.answer.isNotEmpty() &&
                    !uiState.isPlaying,
                onUndo = viewModel::onUndoClicked,
                modifier = Modifier.padding(top = 26.dp),
            )
        }

        FeedbackArea(
            lines = uiState.feedbackLines,
            caption = captionOf(uiState),
            modifier = Modifier.weight(1f).then(gutter).padding(vertical = 16.dp),
        )

        PianoKeyboard(
            octaveRootNote = LEVEL_1_OCTAVE_ROOT,
            highlightedNotes = LEVEL_1_RANGE,
            litNote = uiState.revealedNote,
            onKeyPressed = viewModel::onKeyPressed,
            modifier = Modifier.padding(horizontal = KEYBOARD_GUTTER),
        )

        PracticeActions(
            modifier = gutter.padding(top = 16.dp),
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
 * Scored / Free play as one pill with two halves; the selected half is filled with ink. Disabled
 * (faded) while a melody is being answered: the mode applies from the next melody.
 */
@Composable
private fun ModeToggle(
    mode: AnswerMode,
    enabled: Boolean,
    onSelected: (AnswerMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .height(44.dp)
            .alpha(if (enabled) 1f else 0.55f)
            .clip(CircleShape)
            .background(scheme.surface)
            .border(1.5.dp, scheme.outline, CircleShape)
            .padding(3.dp),
    ) {
        ToggleHalf(text = "Scored", selected = mode == AnswerMode.SCORED, enabled = enabled, onClick = { onSelected(AnswerMode.SCORED) }, modifier = Modifier.weight(1f))
        ToggleHalf(text = "Free play", selected = mode == AnswerMode.FREE, enabled = enabled, onClick = { onSelected(AnswerMode.FREE) }, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ToggleHalf(text: String, selected: Boolean, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxHeight().semantics { this.selected = selected },
        enabled = enabled,
        shape = CircleShape,
        color = if (selected) scheme.onBackground else Color.Transparent,
        contentColor = if (selected) scheme.background else scheme.onSurfaceVariant,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/**
 * One dot per note of the melody, drawn by [DotState] with a shape as well as a colour (so the
 * states read without colour): filled green with a check = right first try, an amber ring with a
 * check = found after wrong presses, filled red with a cross = wrong or revealed, filled ink =
 * played (free play, not checked yet). The dot at [openPosition] (scored: the one the next press
 * answers) gets an accent ring. The undo button (free play only) sits at the end of the row; its
 * slot is always reserved, with a twin on the left, so the dots stay centred.
 */
@Composable
private fun AnswerStrip(
    dots: List<DotState>,
    openPosition: Int?,
    canUndo: Boolean,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DOT_GAP),
    ) {
        Spacer(modifier = Modifier.size(DOT_SIZE))
        Row(horizontalArrangement = Arrangement.spacedBy(DOT_GAP)) {
            dots.forEachIndexed { index, dot ->
                AnswerDot(state = dot, open = index == openPosition)
            }
        }
        Box(modifier = Modifier.size(DOT_SIZE)) {
            if (canUndo) {
                IconSquareButton(onClick = onUndo, contentDescription = "Undo last note") { UndoIcon() }
            }
        }
    }
}

@Composable
private fun AnswerDot(state: DotState, open: Boolean) {
    val colors = HearingTheme.colors
    val scheme = MaterialTheme.colorScheme
    var shape = Modifier.size(DOT_SIZE).clip(CircleShape)
    shape = when (state) {
        DotState.EMPTY -> if (open) {
            shape.background(scheme.primaryContainer).border(3.dp, scheme.primary, CircleShape)
        } else {
            shape.border(2.dp, scheme.outline, CircleShape)
        }
        DotState.FILLED -> shape.background(scheme.onBackground)
        DotState.CORRECT -> shape.background(colors.correct)
        DotState.FOUND -> shape.border(4.dp, colors.found, CircleShape)
        DotState.WRONG -> shape.background(colors.revealed)
    }
    if (open && state != DotState.EMPTY) {
        shape = shape.border(3.dp, scheme.primary, CircleShape)
    }
    Box(modifier = shape, contentAlignment = Alignment.Center) {
        when (state) {
            DotState.CORRECT -> CheckIcon(size = 22.dp, tint = colors.onStatus)
            DotState.FOUND -> CheckIcon(size = 20.dp, tint = colors.found)
            DotState.WRONG -> CrossIcon(size = 20.dp, tint = colors.onStatus)
            DotState.EMPTY, DotState.FILLED -> Unit
        }
    }
}

/**
 * After a scored melody: one card per miss ("3rd note: you played E4, it was F4"), scrollable if
 * a long melody needs it. Otherwise an optional quiet caption.
 */
@Composable
private fun FeedbackArea(lines: List<String>, caption: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        if (lines.isEmpty() && caption != null) {
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        for (line in lines) {
            FeedbackRow(line)
        }
    }
}

@Composable
private fun FeedbackRow(line: String) {
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)
    val parts = line.split(": ", limit = 2)
    val text = buildAnnotatedString {
        if (parts.size == 2) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(parts[0]) }
            append("  ")
            withStyle(SpanStyle(color = scheme.onSurfaceVariant)) { append(parts[1]) }
        } else {
            append(line)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(scheme.surface)
            .border(1.dp, scheme.outline, shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

private data class Status(val title: String, val subtitle: String)

/** The result stays up once a melody is done, even while it plays once more; before that, what's sounding wins. */
private fun statusOf(state: PracticeUiState): Status {
    val position = "Note ${state.currentPosition} of ${state.melodyLength}"
    return when {
        state.phase == PracticePhase.CHECKED -> {
            val replaying = state.playback == Playback.MELODY
            when {
                state.mode == AnswerMode.SCORED -> Status(
                    title = "${state.firstTryCount} of ${state.melodyLength} first try",
                    subtitle = when {
                        replaying -> "Playing it once more"
                        state.firstTryCount == state.melodyLength -> "Every note first try"
                        else -> ""
                    },
                )
                state.allCorrect -> Status("Correct!", "")
                else -> Status("Not quite", if (replaying) "Playing it once more" else "")
            }
        }
        state.playback == Playback.ANSWER -> Status("Your answer", "Listening back")
        state.playback == Playback.REVEAL -> {
            val note = state.revealedNote
            if (note != null) Status("It was ${MidiNote.name(note)}", position) else Status("Listen", position)
        }
        state.isPlaying -> Status("Listen", "Play it back when it ends")
        state.phase == PracticePhase.READY -> Status("Ready when you are", "Tap Play melody to start")
        state.mode == AnswerMode.SCORED -> Status(if (state.wrongPressesAtPosition > 0) "Try again" else "Your turn", position)
        state.isAnswerComplete -> Status("Your turn", "Hear it back, or check it")
        else -> Status("Your turn", "Play it back")
    }
}

/** Free play gets a reminder that it never counts; scored mode explains itself under the keyboard. */
private fun captionOf(state: PracticeUiState): String? = when {
    state.mode == AnswerMode.FREE && state.phase != PracticePhase.CHECKED ->
        "Free play doesn't count toward your score.\nReplay and My answer are unlimited here."
    else -> null
}

/**
 * The buttons under the keyboard. Always the same height (two rows) so the keyboard never jumps.
 * READY shows just "Play melody". Scored: "Replay · n left" on top while a replay is still on
 * offer (a faded "Replay" once the first press has locked it), and the big slot carries a
 * caption while answering (the melody finishes itself) or "Next" once done. Free play: "Replay" /
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
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(BUTTON_GAP)) {
        if (uiState.phase == PracticePhase.READY) {
            Spacer(modifier = Modifier.height(ButtonHeight))
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(BUTTON_GAP)) {
                val showCount = scored && uiState.phase == PracticePhase.ANSWERING &&
                    (uiState.replayAllowed || uiState.replaysLeft == 0)
                val replayLabel = if (showCount) "Replay · ${uiState.replaysLeft} left" else "Replay"
                SecondaryButton(
                    text = replayLabel,
                    enabled = uiState.replayEnabled,
                    onClick = onReplay,
                    icon = { ReplayIcon() },
                    modifier = Modifier.weight(1f),
                )
                if (!scored && Features.HEAR_MY_ANSWER) {
                    SecondaryButton(
                        text = "My answer",
                        enabled = !uiState.isPlaying && uiState.answer.isNotEmpty(),
                        onClick = onHearAnswer,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        val primaryModifier = Modifier.fillMaxWidth()
        when (uiState.phase) {
            PracticePhase.READY -> PrimaryButton(text = "Play melody", onClick = onPlay, modifier = primaryModifier)
            PracticePhase.ANSWERING -> if (scored) {
                ButtonSlot(modifier = primaryModifier) {
                    Text(
                        text = "Every key you press is your answer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                PrimaryButton(
                    text = "Check",
                    onClick = onCheck,
                    enabled = uiState.isAnswerComplete && !uiState.isPlaying,
                    modifier = primaryModifier,
                )
            }
            PracticePhase.CHECKED -> PrimaryButton(text = "Next", onClick = onNext, modifier = primaryModifier)
        }
    }
}
