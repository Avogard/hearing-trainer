package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class MelodyTimingTest {

    private val delta = 1e-9

    @Test
    fun `one note per beat, starting on the beat`() {
        val timed = MelodyTiming.schedule(listOf(60, 62, 64), tempoBpm = 120) // beat = 0.5 s
        assertEquals(listOf(60, 62, 64), timed.map { it.note })
        assertEquals(listOf(0.0, 0.5, 1.0), timed.map { it.startSeconds })
    }

    @Test
    fun `notes are released before the next beat so they don't blur together`() {
        val timed = MelodyTiming.schedule(listOf(60, 62), tempoBpm = 60) // beat = 1 s
        for (note in timed) {
            assertTrue(note.durationSeconds > 0.0)
            assertTrue(note.durationSeconds < MelodyTiming.beatSeconds(60))
        }
        assertEquals(Config.MELODY_NOTE_GATE, timed[0].durationSeconds, delta)
    }

    @Test
    fun `total playback time covers every beat plus the trailing pause`() {
        val total = MelodyTiming.totalSeconds(noteCount = 4, tempoBpm = 120)
        assertEquals(4 * 0.5 + Config.PAUSE_AFTER_PLAYBACK_SECONDS, total, delta)
    }

    @Test
    fun `an empty list schedules nothing`() {
        assertTrue(MelodyTiming.schedule(emptyList(), tempoBpm = 90).isEmpty())
    }

    @Test
    fun `rejects a non-positive tempo`() {
        assertThrows(IllegalArgumentException::class.java) { MelodyTiming.schedule(listOf(60), tempoBpm = 0) }
    }
}
