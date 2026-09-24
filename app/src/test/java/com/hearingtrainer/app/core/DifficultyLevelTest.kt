package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DifficultyLevelTest {

    @Test
    fun `level1 produces a real variety of melodies, not almost always the same one`() {
        // Regression test: with maxInterval = 2 (the original value), the root sitting at the
        // exact bottom of the C4-G4 range meant it had only one in-range, in-scale step
        // neighbor (D4), so the first two notes were forced every time and only the third note
        // varied — 2 possible melodies total out of 200 seeds. See docs/DECISIONS.md.
        val melodies = (0 until 200)
            .map { seed -> MelodyGenerator.generate(DifficultyLevel.level1(seed = seed.toLong())).notes }
            .toSet()
        assertTrue(
            "expected real variety across 200 seeds, got only $melodies",
            melodies.size >= 5,
        )
    }

    @Test
    fun `level1 always starts on the root, stays in range and has the default length`() {
        for (seed in 0 until 50) {
            val notes = MelodyGenerator.generate(DifficultyLevel.level1(seed = seed.toLong())).notes
            assertTrue("seed $seed: $notes", notes.first() == 60)
            assertTrue("seed $seed: $notes", notes.all { it in 60..67 })
            assertEquals("seed $seed: $notes", Config.DEFAULT_MELODY_LENGTH, notes.size)
        }
    }

    @Test
    fun `level1 honours every melody length the Settings screen allows`() {
        for (length in Config.MIN_MELODY_LENGTH..Config.MAX_MELODY_LENGTH) {
            for (seed in 0 until 20) {
                val notes = MelodyGenerator.generate(DifficultyLevel.level1(seed = seed.toLong(), length = length)).notes
                assertEquals("length $length, seed $seed: $notes", length, notes.size)
                assertTrue("length $length, seed $seed: $notes", notes.all { it in 60..67 })
            }
        }
    }
}
