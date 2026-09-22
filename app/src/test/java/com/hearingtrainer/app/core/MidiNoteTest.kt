package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Test

class MidiNoteTest {

    @Test
    fun `middle C is C4`() {
        assertEquals("C4", MidiNote.name(60))
    }

    @Test
    fun `sharps are named with a hash`() {
        assertEquals("C#4", MidiNote.name(61))
    }

    @Test
    fun `octave boundary around middle C`() {
        assertEquals("B3", MidiNote.name(59))
        assertEquals("C5", MidiNote.name(72))
    }
}
