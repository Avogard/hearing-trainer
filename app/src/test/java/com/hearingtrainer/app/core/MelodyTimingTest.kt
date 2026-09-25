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
    fun `a start offset shifts every note by whole beats`() {
        val timed = MelodyTiming.schedule(listOf(60, 62), tempoBpm = 120, startBeat = 5.0) // beat = 0.5 s
        assertEquals(listOf(2.5, 3.0), timed.map { it.startSeconds })
    }

    @Test
    fun `chords put all their notes on the same beat`() {
        val timed = MelodyTiming.scheduleChords(listOf(listOf(48, 52, 55), listOf(53, 57, 60)), tempoBpm = 60)
        assertEquals(listOf(48, 52, 55, 53, 57, 60), timed.map { it.note })
        assertEquals(listOf(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), timed.map { it.startSeconds })
        for (note in timed) {
            assertEquals(Config.MELODY_NOTE_GATE, note.durationSeconds, delta)
            assertEquals(1f, note.gain)
        }
        assertTrue(MelodyTiming.scheduleChords(listOf(listOf(48, 52)), 60, gain = 0.5f).all { it.gain == 0.5f })
    }

    @Test
    fun `cadence, then a gap, then the melody, in one sequence`() {
        val cadence = listOf(listOf(48, 52, 55), listOf(53, 57, 60), listOf(55, 59, 62), listOf(48, 52, 55))
        val timed = MelodyTiming.scheduleWithCadence(cadence, listOf(60, 62, 64), tempoBpm = 60) // beat = 1 s
        assertEquals(12 + 3, timed.size)
        val melodyStart = 4 + Config.CADENCE_GAP_BEATS
        assertEquals(listOf(melodyStart, melodyStart + 1, melodyStart + 2), timed.takeLast(3).map { it.startSeconds })
        assertEquals(listOf(60, 62, 64), timed.takeLast(3).map { it.note })
        // The gap is real silence: the last chord is released before the melody's first note.
        val lastChordEnd = timed[11].startSeconds + timed[11].durationSeconds
        assertTrue(lastChordEnd < melodyStart)
        // Chord tones are quieter than melody notes, which play at full level.
        assertTrue(timed.take(12).all { it.gain == Config.CADENCE_GAIN })
        assertTrue(timed.takeLast(3).all { it.gain == 1f })
        // Total playback time counts every beat: 4 chords + gap + 3 notes, plus the pause.
        assertEquals((4 + Config.CADENCE_GAP_BEATS + 3) + Config.PAUSE_AFTER_PLAYBACK_SECONDS, MelodyTiming.totalSeconds(timed, 60), delta)
    }

    @Test
    fun `without a cadence the sequence is just the melody`() {
        assertEquals(MelodyTiming.schedule(listOf(60, 62), 90), MelodyTiming.scheduleWithCadence(emptyList(), listOf(60, 62), 90))
    }

    @Test
    fun `total time of a sequence agrees with the note-count form for a plain melody`() {
        val timed = MelodyTiming.schedule(listOf(60, 62, 64, 65), tempoBpm = 120)
        assertEquals(MelodyTiming.totalSeconds(noteCount = 4, tempoBpm = 120), MelodyTiming.totalSeconds(timed, 120), delta)
        assertEquals(Config.PAUSE_AFTER_PLAYBACK_SECONDS, MelodyTiming.totalSeconds(emptyList(), 120), delta)
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
