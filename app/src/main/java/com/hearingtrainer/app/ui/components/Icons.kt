package com.hearingtrainer.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// The app's handful of icons, drawn with Canvas on a 24-unit grid (the same strokes as the design
// canvas). Material 3 no longer bundles an icon set, and six icons don't justify a dependency.

/** Runs [draw] with `u` = one grid unit in pixels and a round-capped stroke of [strokeWidth]. */
@Composable
private fun StrokeIcon(
    modifier: Modifier,
    size: Dp,
    strokeWidth: Dp,
    draw: DrawScope.(u: Float, stroke: Stroke) -> Unit,
) {
    Canvas(modifier = modifier.size(size)) {
        val u = this.size.minDimension / 24f
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        draw(u, stroke)
    }
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, size: Dp = 22.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.dp) { u, stroke ->
        val path = Path().apply { moveTo(15 * u, 5 * u); lineTo(8 * u, 12 * u); lineTo(15 * u, 19 * u) }
        drawPath(path, tint, style = stroke)
    }
}

/** Three sliders: the Settings entry. */
@Composable
fun SlidersIcon(modifier: Modifier = Modifier, size: Dp = 22.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.dp) { u, stroke ->
        for (y in listOf(6f, 12f, 18f)) {
            drawLine(tint, Offset(4 * u, y * u), Offset(20 * u, y * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
        }
        for ((x, y) in listOf(15f to 6f, 9f to 12f, 17f to 18f)) {
            drawCircle(tint, radius = 2.5f * u, center = Offset(x * u, y * u))
        }
    }
}

@Composable
fun CheckIcon(modifier: Modifier = Modifier, size: Dp = 22.dp, strokeWidth: Dp = 2.5.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, strokeWidth) { u, stroke ->
        val path = Path().apply { moveTo(5 * u, 12.5f * u); lineTo(9.5f * u, 17 * u); lineTo(19 * u, 7.5f * u) }
        drawPath(path, tint, style = stroke)
    }
}

@Composable
fun CrossIcon(modifier: Modifier = Modifier, size: Dp = 20.dp, strokeWidth: Dp = 2.5.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, strokeWidth) { u, stroke ->
        drawLine(tint, Offset(6 * u, 6 * u), Offset(18 * u, 18 * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
        drawLine(tint, Offset(18 * u, 6 * u), Offset(6 * u, 18 * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
    }
}

@Composable
fun UndoIcon(modifier: Modifier = Modifier, size: Dp = 20.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.dp) { u, stroke ->
        val head = Path().apply { moveTo(9 * u, 14 * u); lineTo(4 * u, 9 * u); lineTo(9 * u, 4 * u) }
        val tail = Path().apply {
            moveTo(4 * u, 9 * u)
            lineTo(13 * u, 9 * u)
            arcTo(Rect(7 * u, 9 * u, 19 * u, 21 * u), startAngleDegrees = -90f, sweepAngleDegrees = 180f, forceMoveTo = false)
            lineTo(10 * u, 21 * u)
        }
        drawPath(head, tint, style = stroke)
        drawPath(tail, tint, style = stroke)
    }
}

/** A counter-clockwise arrow: Replay. */
@Composable
fun ReplayIcon(modifier: Modifier = Modifier, size: Dp = 20.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.dp) { u, stroke ->
        val arc = Path().apply {
            arcTo(Rect(3 * u, 3 * u, 21 * u, 21 * u), startAngleDegrees = 180f, sweepAngleDegrees = -312f, forceMoveTo = true)
        }
        val head = Path().apply { moveTo(3 * u, 4 * u); lineTo(3 * u, 9 * u); lineTo(8 * u, 9 * u) }
        drawPath(arc, tint, style = stroke)
        drawPath(head, tint, style = stroke)
    }
}

@Composable
fun MinusIcon(modifier: Modifier = Modifier, size: Dp = 26.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.25.dp) { u, stroke ->
        drawLine(tint, Offset(5 * u, 12 * u), Offset(19 * u, 12 * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
    }
}

@Composable
fun PlusIcon(modifier: Modifier = Modifier, size: Dp = 26.dp, tint: Color = LocalContentColor.current) {
    StrokeIcon(modifier, size, 2.25.dp) { u, stroke ->
        drawLine(tint, Offset(12 * u, 5 * u), Offset(12 * u, 19 * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
        drawLine(tint, Offset(5 * u, 12 * u), Offset(19 * u, 12 * u), strokeWidth = stroke.width, cap = StrokeCap.Round)
    }
}
