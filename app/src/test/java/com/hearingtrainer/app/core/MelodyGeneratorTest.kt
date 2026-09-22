package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class MelodyGeneratorTest {

    // Matches difficulty Level 1 in docs/SPEC.md: C major, 3 notes, C4-G4, steps only.
    private fun level1Spec(seed: Long = 1L) = MelodySpec(
        rootNote = 60, // C4
        scale = Scale.MAJOR,
        lowestNote = 60,
        highestNote = 67, // G4
        length = 3,
        maxInterval = 2,
        startOnRootOnly = true,
        allowImmediateRepeats = false,
        seed = seed,
    )

    // A wider spec for tests that need room to explore (interval/repeat behavior near
    // boundaries is easy to get wrong, so exercise it over a bigger range and more notes).
    private fun wideSpec(seed: Long) = level1Spec(seed).copy(
        length = 20,
        lowestNote = 48, // C3
        highestNote = 72, // C5
        maxInterval = 4,
    )

    @Test
    fun `generates the requested number of notes`() {
        val melody = MelodyGenerator.generate(level1Spec())
        assertEquals(3, melody.notes.size)
    }

    @Test
    fun `all notes are within the given range`() {
        val spec = level1Spec()
        val melody = MelodyGenerator.generate(spec)
        melody.notes.forEach { note ->
            assertTrue("$note out of range ${spec.lowestNote}..${spec.highestNote}", note in spec.lowestNote..spec.highestNote)
        }
    }

    @Test
    fun `all notes belong to the scale`() {
        val spec = level1Spec()
        val melody = MelodyGenerator.generate(spec)
        melody.notes.forEach { note ->
            assertTrue("$note is not in ${spec.scale} rooted at ${spec.rootNote}", spec.scale.contains(note, spec.rootNote))
        }
    }

    @Test
    fun `starts on the root when startOnRootOnly is true`() {
        val melody = MelodyGenerator.generate(level1Spec())
        assertEquals(60, melody.notes.first())
    }

    @Test
    fun `never repeats a note immediately when repeats are disallowed`() {
        val melody = MelodyGenerator.generate(wideSpec(seed = 7L))
        melody.notes.zipWithNext().forEach { (a, b) ->
            assertNotEquals("note repeated immediately", a, b)
        }
    }

    @Test
    fun `consecutive notes never exceed maxInterval`() {
        val spec = wideSpec(seed = 7L)
        val melody = MelodyGenerator.generate(spec)
        melody.notes.zipWithNext().forEach { (a, b) ->
            assertTrue("interval ${Math.abs(a - b)} exceeds ${spec.maxInterval}", Math.abs(a - b) <= spec.maxInterval)
        }
    }

    @Test
    fun `same seed produces the same melody`() {
        val a = MelodyGenerator.generate(wideSpec(seed = 42L))
        val b = MelodyGenerator.generate(wideSpec(seed = 42L))
        assertEquals(a, b)
    }

    @Test
    fun `different seeds can produce different melodies`() {
        val a = MelodyGenerator.generate(wideSpec(seed = 1L))
        val b = MelodyGenerator.generate(wideSpec(seed = 2L))
        assertNotEquals(a, b)
    }

    @Test
    fun `rejects an inverted or empty range`() {
        assertThrows(IllegalArgumentException::class.java) {
            level1Spec().copy(lowestNote = 70, highestNote = 60)
        }
    }

    @Test
    fun `throws when neither the root nor its fifth falls in range`() {
        // C major root (60) and its fifth (67) are both outside 61..63, even though the
        // range itself isn't empty of scale notes (62, D, is in C major).
        assertThrows(IllegalStateException::class.java) {
            MelodyGenerator.generate(level1Spec().copy(lowestNote = 61, highestNote = 63))
        }
    }
}
