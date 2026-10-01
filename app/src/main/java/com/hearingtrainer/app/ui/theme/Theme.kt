package com.hearingtrainer.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AccentLight,
    onPrimary = Color.White,
    primaryContainer = AccentSoftLight,
    onPrimaryContainer = InkLight,
    secondary = InkLight,
    onSecondary = PaperLight,
    background = PaperLight,
    onBackground = InkLight,
    surface = SurfaceLight,
    onSurface = InkLight,
    surfaceVariant = SoftLight,
    onSurfaceVariant = MutedLight,
    outline = LineLight,
    outlineVariant = LineLight,
    error = Color(0xFFC8402B),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentDark,
    onPrimary = OnAccentDark,
    primaryContainer = AccentSoftDark,
    onPrimaryContainer = InkDark,
    secondary = InkDark,
    onSecondary = PaperDark,
    background = PaperDark,
    onBackground = InkDark,
    surface = SurfaceDark,
    onSurface = InkDark,
    surfaceVariant = SoftDark,
    onSurfaceVariant = MutedDark,
    outline = LineDark,
    outlineVariant = LineDark,
    error = Color(0xFFE3624C),
    onError = Color(0xFF12110F),
)

/**
 * The app's theme. No Material "dynamic colour": on Android 12+ that would replace the palette
 * with one derived from the wallpaper, and the paper-and-ink look is the design.
 */
@Composable
fun HearingTrainerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val hearingColors = if (darkTheme) DarkHearingColors else LightHearingColors
    CompositionLocalProvider(LocalHearingColors provides hearingColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

/** Access to the colours beyond Material's scheme: `HearingTheme.colors.correct`. */
object HearingTheme {
    val colors: HearingColors
        @Composable get() = LocalHearingColors.current
}
