package com.hearingtrainer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// The palette from the "Hearing Trainer UI" design canvas (see docs/DECISIONS.md, 2026-10-01):
// a warm paper ground, near-black ink, one blue accent, three feedback colours. Light first,
// then the same roles in the dark theme.

val PaperLight = Color(0xFFF6F3EE)
val SurfaceLight = Color(0xFFFFFFFF)
val InkLight = Color(0xFF1C1B18)
val MutedLight = Color(0xFF625D53)
val LineLight = Color(0xFFE3DED5)
val SoftLight = Color(0xFFECE8E0)
val AccentLight = Color(0xFF2C56C9)
val AccentSoftLight = Color(0xFFD6DAE8)

val PaperDark = Color(0xFF151412)
val SurfaceDark = Color(0xFF201F1B)
val InkDark = Color(0xFFF2EFE8)
val MutedDark = Color(0xFFA39D91)
val LineDark = Color(0xFF33312B)
val SoftDark = Color(0xFF2A2924)
val AccentDark = Color(0xFF7C9BFF)
val OnAccentDark = Color(0xFF0F1730)
val AccentSoftDark = Color(0xFF262A38)

/**
 * The colours Material's scheme has no name for: the answer-strip states and the piano keys.
 * Read them through [HearingTheme.colors]; the theme picks the light or dark set.
 */
@Immutable
data class HearingColors(
    /** A note right at the first try. */
    val correct: Color,
    /** A note found after one or more wrong presses. */
    val found: Color,
    /** A note revealed by the app, or wrong at Check. */
    val revealed: Color,
    /** The check / cross drawn on top of [correct] and [revealed]. */
    val onStatus: Color,
    val whiteKey: Color,
    val whiteKeyDimmed: Color,
    val whiteKeyLabel: Color,
    val whiteKeyDimmedLabel: Color,
    val blackKey: Color,
    val blackKeyDimmed: Color,
    val blackKeyLabel: Color,
    val keyBorder: Color,
)

val LightHearingColors = HearingColors(
    correct = Color(0xFF1F8A4C),
    found = Color(0xFFC67A00),
    revealed = Color(0xFFC8402B),
    onStatus = Color.White,
    whiteKey = Color(0xFFFFFFFF),
    whiteKeyDimmed = Color(0xFFEFEBE4),
    whiteKeyLabel = Color(0xFF625D53),
    whiteKeyDimmedLabel = Color(0xFF6E685C),
    blackKey = Color(0xFF2B2A26),
    blackKeyDimmed = Color(0xFF6B675D),
    blackKeyLabel = Color(0xFFF2EFE8),
    keyBorder = Color(0xFFD9D3C8),
)

val DarkHearingColors = HearingColors(
    correct = Color(0xFF3FB56E),
    found = Color(0xFFE0A030),
    revealed = Color(0xFFE3624C),
    onStatus = Color(0xFF12110F),
    whiteKey = Color(0xFFF2EEE6),
    whiteKeyDimmed = Color(0xFF8F8A80),
    whiteKeyLabel = Color(0xFF4A463E),
    whiteKeyDimmedLabel = Color(0xFF1C1B18),
    blackKey = Color(0xFF0C0C0B),
    blackKeyDimmed = Color(0xFF4E4B44),
    blackKeyLabel = Color(0xFFF2EFE8),
    keyBorder = Color(0xFF3A3832),
)

val LocalHearingColors = staticCompositionLocalOf { LightHearingColors }
