package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScaleTest {

    @Test
    fun `major scale contains the expected pitch classes relative to its root`() {
        // C major: C D E F G A B
        assertTrue(Scale.MAJOR.contains(60, rootNote = 60)) // C
        assertTrue(Scale.MAJOR.contains(62, rootNote = 60)) // D
        assertFalse(Scale.MAJOR.contains(61, rootNote = 60)) // C#
    }

    @Test
    fun `scale membership repeats every octave`() {
        assertTrue(Scale.MAJOR.contains(72, rootNote = 60)) // C an octave up
        assertTrue(Scale.MAJOR.contains(48, rootNote = 60)) // C an octave down
    }

    @Test
    fun `natural minor scale contains the expected pitch classes`() {
        // A natural minor: A B C D E F G
        assertTrue(Scale.NATURAL_MINOR.contains(69, rootNote = 69)) // A
        assertTrue(Scale.NATURAL_MINOR.contains(72, rootNote = 69)) // C
        assertFalse(Scale.NATURAL_MINOR.contains(73, rootNote = 69)) // C#
    }

    @Test
    fun `fifth above root is seven semitones up`() {
        assertEquals(67, Scale.MAJOR.fifthAbove(60))
    }
}
