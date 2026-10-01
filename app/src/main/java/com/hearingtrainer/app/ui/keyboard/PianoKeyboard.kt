package com.hearingtrainer.app.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hearingtrainer.app.core.MidiNote
import com.hearingtrainer.app.ui.theme.HearingTheme

// Semitone offsets within one octave.
private val WHITE_KEY_OFFSETS = listOf(0, 2, 4, 5, 7, 9, 11) // C D E F G A B
private val BLACK_KEY_OFFSETS = listOf(1, 3, 6, 8, 10) // C# D# F# G# A#

// Each black key sits in the gap after this white key (0-based index into WHITE_KEY_OFFSETS) —
// there's no black key after E or after B, matching a real keyboard.
private val BLACK_KEY_AFTER_WHITE_INDEX = mapOf(1 to 0, 3 to 1, 6 to 3, 8 to 4, 10 to 5)

private val KEYBOARD_HEIGHT: Dp = 224.dp
private val KEY_GAP: Dp = 3.dp
private const val BLACK_KEY_WIDTH_FRACTION = 0.6f
private const val BLACK_KEY_HEIGHT_FRACTION = 0.6f
private val WHITE_KEY_SHAPE = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
private val BLACK_KEY_SHAPE = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)

/**
 * A one-octave on-screen piano keyboard starting at [octaveRootNote] (e.g. 60 for the C4-B4
 * octave). Keys not in [highlightedNotes] are greyed but still fully playable — per docs/SPEC.md:
 * "Keys not in the current scale are dimmed on early levels (still playable on later levels)".
 * [litNote], if any, is drawn in the accent colour: the key the app is pointing at while it
 * reveals a note. White keys share the available width and carry their note name; the Practice
 * screen gives the keyboard nearly the full screen width so seven of them are at least 48 dp
 * wide on a 360 dp phone (docs/SPEC.md).
 */
@Composable
fun PianoKeyboard(
    octaveRootNote: Int,
    highlightedNotes: Set<Int>,
    onKeyPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
    litNote: Int? = null,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(KEYBOARD_HEIGHT)) {
        val whiteKeyWidth = (maxWidth - KEY_GAP * (WHITE_KEY_OFFSETS.size - 1)) / WHITE_KEY_OFFSETS.size
        val blackKeyWidth = whiteKeyWidth * BLACK_KEY_WIDTH_FRACTION

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(KEY_GAP)) {
            WHITE_KEY_OFFSETS.forEach { offset ->
                val note = octaveRootNote + offset
                WhiteKey(
                    note = note,
                    dimmed = note !in highlightedNotes,
                    lit = note == litNote,
                    onPressed = onKeyPressed,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
        }

        BLACK_KEY_OFFSETS.forEach { offset ->
            val note = octaveRootNote + offset
            val afterWhiteIndex = BLACK_KEY_AFTER_WHITE_INDEX.getValue(offset)
            // Centre of the gap between white key afterWhiteIndex and the next one.
            val centerX = whiteKeyWidth * (afterWhiteIndex + 1) + KEY_GAP * afterWhiteIndex + KEY_GAP / 2
            BlackKey(
                note = note,
                dimmed = note !in highlightedNotes,
                lit = note == litNote,
                onPressed = onKeyPressed,
                modifier = Modifier
                    .offset(x = centerX - blackKeyWidth / 2)
                    .width(blackKeyWidth)
                    .fillMaxHeight(BLACK_KEY_HEIGHT_FRACTION),
            )
        }
    }
}

@Composable
private fun WhiteKey(note: Int, dimmed: Boolean, lit: Boolean, onPressed: (Int) -> Unit, modifier: Modifier = Modifier) {
    val colors = HearingTheme.colors
    val scheme = MaterialTheme.colorScheme
    val background = when {
        lit -> scheme.primary
        dimmed -> colors.whiteKeyDimmed
        else -> colors.whiteKey
    }
    val labelColor = when {
        lit -> scheme.onPrimary
        dimmed -> colors.whiteKeyDimmedLabel
        else -> colors.whiteKeyLabel
    }
    Key(
        note = note,
        label = MidiNote.name(note),
        background = background,
        border = colors.keyBorder,
        shape = WHITE_KEY_SHAPE,
        labelColor = labelColor,
        labelSize = 13.sp,
        onPressed = onPressed,
        modifier = modifier.padding(bottom = 12.dp),
    )
}

@Composable
private fun BlackKey(note: Int, dimmed: Boolean, lit: Boolean, onPressed: (Int) -> Unit, modifier: Modifier = Modifier) {
    val colors = HearingTheme.colors
    val scheme = MaterialTheme.colorScheme
    val background = when {
        lit -> scheme.primary
        dimmed -> colors.blackKeyDimmed
        else -> colors.blackKey
    }
    Key(
        note = note,
        // "C♯" without the octave: the key is narrow.
        label = MidiNote.name(note).dropLast(1).replace('#', '♯'),
        background = background,
        border = null,
        shape = BLACK_KEY_SHAPE,
        labelColor = if (lit) scheme.onPrimary else colors.blackKeyLabel,
        labelSize = 11.sp,
        onPressed = onPressed,
        modifier = modifier.padding(bottom = 10.dp),
    )
}

@Composable
private fun Key(
    note: Int,
    label: String,
    background: Color,
    border: Color?,
    shape: RoundedCornerShape,
    labelColor: Color,
    labelSize: TextUnit,
    onPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spokenName = MidiNote.name(note).replace("#", " sharp ")
    Box(
        modifier = Modifier
            .clip(shape)
            .background(background)
            .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
            .clickable { onPressed(note) }
            .semantics { contentDescription = spokenName }
            .then(modifier),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = labelSize, letterSpacing = 0.sp),
            color = labelColor,
        )
    }
}
