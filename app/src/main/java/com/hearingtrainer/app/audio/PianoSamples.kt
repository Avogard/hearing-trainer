package com.hearingtrainer.app.audio

import com.hearingtrainer.app.R

/**
 * Which bundled sample plays which MIDI note. One WAV per note, C3..C6, all rendered by
 * tools/render_piano_samples.py (see docs/DECISIONS.md). Nothing is pitch-shifted at runtime, so
 * every note has exactly the timbre it was rendered with.
 *
 * To use real recordings instead: drop in files with the same names (mono 16-bit PCM WAV,
 * 48 kHz) and nothing else changes.
 */
internal object PianoSamples {
    val RES_IDS: Map<Int, Int> = mapOf(
        48 to R.raw.piano_048,
        49 to R.raw.piano_049,
        50 to R.raw.piano_050,
        51 to R.raw.piano_051,
        52 to R.raw.piano_052,
        53 to R.raw.piano_053,
        54 to R.raw.piano_054,
        55 to R.raw.piano_055,
        56 to R.raw.piano_056,
        57 to R.raw.piano_057,
        58 to R.raw.piano_058,
        59 to R.raw.piano_059,
        60 to R.raw.piano_060,
        61 to R.raw.piano_061,
        62 to R.raw.piano_062,
        63 to R.raw.piano_063,
        64 to R.raw.piano_064,
        65 to R.raw.piano_065,
        66 to R.raw.piano_066,
        67 to R.raw.piano_067,
        68 to R.raw.piano_068,
        69 to R.raw.piano_069,
        70 to R.raw.piano_070,
        71 to R.raw.piano_071,
        72 to R.raw.piano_072,
        73 to R.raw.piano_073,
        74 to R.raw.piano_074,
        75 to R.raw.piano_075,
        76 to R.raw.piano_076,
        77 to R.raw.piano_077,
        78 to R.raw.piano_078,
        79 to R.raw.piano_079,
        80 to R.raw.piano_080,
        81 to R.raw.piano_081,
        82 to R.raw.piano_082,
        83 to R.raw.piano_083,
        84 to R.raw.piano_084,
    )
}
