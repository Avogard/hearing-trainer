package com.hearingtrainer.app.core

/**
 * Utilities for MIDI note numbers. Per project convention (see CLAUDE.md), a note is always
 * an Int MIDI number, with 60 = middle C, matching scientific pitch notation ("C4").
 */
object MidiNote {
    private val NAMES = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

    /** e.g. 60 -> "C4", 61 -> "C#4", 59 -> "B3". Mainly for logs, debugging and tests. */
    fun name(note: Int): String {
        val pitchClass = Math.floorMod(note, 12)
        val octave = Math.floorDiv(note, 12) - 1
        return "${NAMES[pitchClass]}$octave"
    }
}
