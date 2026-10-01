package com.hearingtrainer.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// The three button kinds from the design: one filled primary per screen, bordered secondaries,
// and 44 dp bordered squares for icons. All at least 44 dp tall (one-handed use, CLAUDE.md).

val ButtonHeight = 56.dp
val BigButtonHeight = 64.dp
private val ButtonShape = RoundedCornerShape(18.dp)
private const val DISABLED_ALPHA = 0.45f

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = ButtonHeight,
) {
    val scheme = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = scheme.primary,
            contentColor = scheme.onPrimary,
            disabledContainerColor = scheme.primary.copy(alpha = DISABLED_ALPHA),
            disabledContentColor = scheme.onPrimary.copy(alpha = 0.9f),
        ),
        elevation = null,
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val borderColor = if (enabled) scheme.outline else scheme.outline.copy(alpha = DISABLED_ALPHA)
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        enabled = enabled,
        shape = ButtonShape,
        border = BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = scheme.surface,
            contentColor = scheme.onSurface,
            disabledContainerColor = scheme.surface.copy(alpha = DISABLED_ALPHA),
            disabledContentColor = scheme.onSurface.copy(alpha = DISABLED_ALPHA),
        ),
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

/** A 44 dp bordered square holding one icon: Back, Settings, Undo. */
@Composable
fun IconSquareButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = modifier.size(44.dp).semantics { this.contentDescription = contentDescription },
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        color = scheme.surface,
        contentColor = scheme.onSurface,
        border = BorderStroke(1.5.dp, scheme.outline),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
    }
}

/** The 64 dp tonal square of the Settings steppers. */
@Composable
fun StepperButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val alpha = if (enabled) 1f else DISABLED_ALPHA
    Surface(
        onClick = onClick,
        modifier = modifier.size(64.dp).semantics { this.contentDescription = contentDescription },
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        color = scheme.surfaceVariant.copy(alpha = alpha),
        contentColor = scheme.onSurface.copy(alpha = alpha),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
    }
}

/** Keeps a button row's height when a slot is intentionally empty, so the keyboard never jumps. */
@Composable
fun ButtonSlot(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    Row(modifier = modifier.height(ButtonHeight), verticalAlignment = Alignment.CenterVertically) { content() }
}
