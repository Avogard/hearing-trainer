package com.hearingtrainer.app.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Semitone offsets within one octave.
private val WHITE_KEY_OFFSETS = listOf(0, 2, 4, 5, 7, 9, 11) // C D E F G A B
private val BLACK_KEY_OFFSETS = listOf(1, 3, 6, 8, 10) // C# D# F# G# A#

// Each black key sits just after this white key (0-based index into WHITE_KEY_OFFSETS) — there's
// no black key after E or after B, matching a real keyboard.
private val BLACK_KEY_AFTER_WHITE_INDEX = mapOf(1 to 0, 3 to 1, 6 to 3, 8 to 4, 10 to 5)

private val KEYBOARD_HEIGHT: Dp = 200.dp

/**
 * A one-octave on-screen piano keyboard starting at [octaveRootNote] (e.g. 60 for the C4-B4
 * octave). Keys not in [highlightedNotes] are dimmed but still fully playable — per docs/SPEC.md:
 * "Keys not in the current scale are dimmed on early levels (still playable on later levels)".
 * White keys fill the available width; on any normal phone width that comfortably clears
 * SPEC's "at least 48dp wide" requirement.
 */
@Composable
fun PianoKeyboard(
    octaveRootNote: Int,
    highlightedNotes: Set<Int>,
    onKeyPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(KEYBOARD_HEIGHT)) {
        val whiteKeyWidth = maxWidth / WHITE_KEY_OFFSETS.size
        val blackKeyWidth = whiteKeyWidth * 0.6f

        Row(modifier = Modifier.fillMaxSize()) {
            WHITE_KEY_OFFSETS.forEach { offset ->
                val note = octaveRootNote + offset
                PianoKey(
                    note = note,
                    isWhite = true,
                    dimmed = note !in highlightedNotes,
                    onPressed = onKeyPressed,
                    modifier = Modifier.width(whiteKeyWidth).fillMaxHeight(),
                )
            }
        }

        BLACK_KEY_OFFSETS.forEach { offset ->
            val note = octaveRootNote + offset
            val afterWhiteIndex = BLACK_KEY_AFTER_WHITE_INDEX.getValue(offset)
            val centerX = whiteKeyWidth * (afterWhiteIndex + 1)
            PianoKey(
                note = note,
                isWhite = false,
                dimmed = note !in highlightedNotes,
                onPressed = onKeyPressed,
                modifier = Modifier
                    .offset(x = centerX - blackKeyWidth / 2)
                    .width(blackKeyWidth)
                    .fillMaxHeight(0.6f),
            )
        }
    }
}

@Composable
private fun PianoKey(
    note: Int,
    isWhite: Boolean,
    dimmed: Boolean,
    onPressed: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseColor = if (isWhite) Color.White else Color.Black
    val color = if (dimmed) baseColor.copy(alpha = if (isWhite) 0.55f else 0.75f) else baseColor
    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)
            .clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
            .background(color)
            .clickable { onPressed(note) },
    )
}
