package com.hearingtrainer.app.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TonalContextTest {

    private val delta = 1e-9

    // Root and the lowest melody note for C, G and F major, each with the melody starting on the root
    // in the octave around middle C (as Level 1 does for C).
    private val keys = mapOf("C" to 60, "G" to 67, "F" to 65)

    @Test
    fun `C major under a C4 melody is exactly the voicing from the spec`() {
        assertEquals(
            listOf(
                listOf(48, 52, 55), // C3 E3 G3
                listOf(53, 57, 60), // F3 A3 C4
                listOf(55, 59, 62), // G3 B3 D4
                listOf(48, 52, 55), // C3 E3 G3
            ),
            TonalContext.cadenceChords(rootMidi = 60, melodyLowest = 60),
        )
    }

    @Test
    fun `four chords of three notes, all in the key`() {
        for ((name, root) in keys) {
            val chords = TonalContext.cadenceChords(rootMidi = root, melodyLowest = root)
            assertEquals("$name major", 4, chords.size)
            for (chord in chords) {
                assertEquals("$name major: $chord", 3, chord.size)
                for (note in chord) {
                    assertTrue("$name major: ${MidiNote.name(note)} is not in the key", Scale.MAJOR.contains(note, root))
                }
            }
        }
    }

    // The next two hold when the melody's range starts on the root, as every level in docs/SPEC.md
    // does. A range starting on another degree still gets a cadence in the octave below its lowest
    // note, but the tonic chord may then reach into it.
    @Test
    fun `chord roots are I, IV, V, I in the octave directly below a melody that starts on the root`() {
        for ((name, root) in keys) {
            val chords = TonalContext.cadenceChords(rootMidi = root, melodyLowest = root)
            val roots = chords.map { it.first() }
            assertEquals("$name major roots", listOf(root - 12, root - 12 + 5, root - 12 + 7, root - 12), roots)
            for (chordRoot in roots) {
                assertTrue("$name major: root ${MidiNote.name(chordRoot)}", chordRoot in (root - 12) until root)
            }
        }
    }

    @Test
    fun `each chord is a root-position major triad`() {
        for ((name, root) in keys) {
            for (chord in TonalContext.cadenceChords(rootMidi = root, melodyLowest = root)) {
                assertEquals("$name major: $chord", listOf(0, 4, 7), chord.map { it - chord[0] })
            }
        }
    }

    @Test
    fun `the tonic chords sit entirely below a melody that starts on the root`() {
        for ((name, root) in keys) {
            val melodyLowest = root
            val chords = TonalContext.cadenceChords(rootMidi = root, melodyLowest = melodyLowest)
            for (note in chords.first() + chords.last()) {
                assertTrue("$name major: ${MidiNote.name(note)} not below ${MidiNote.name(melodyLowest)}", note < melodyLowest)
            }
        }
    }

    @Test
    fun `the register follows the melody, not the root's octave`() {
        // A C-major melody living an octave higher gets the cadence an octave higher too.
        assertEquals(listOf(60, 64, 67), TonalContext.cadenceChords(rootMidi = 60, melodyLowest = 72).first())
        // A C-major melody whose range starts on E4: the tonic root is the highest C strictly below
        // E4, which is C4 — the cadence then overlaps the bottom of the range.
        assertEquals(listOf(60, 64, 67), TonalContext.cadenceChords(rootMidi = 60, melodyLowest = 64).first())
        // The root itself is never "below" itself: a C4 melody gets C3, not C4.
        assertEquals(48, TonalContext.cadenceChords(rootMidi = 60, melodyLowest = 60).first().first())
    }

    @Test
    fun `never goes below the sampled range`() {
        // C3 is the lowest sample and is still used (the boundary is inclusive)...
        val cMajor = TonalContext.cadenceChords(rootMidi = 60, melodyLowest = Config.LOWEST_SAMPLED_NOTE + 3)
        assertEquals(Config.LOWEST_SAMPLED_NOTE, cMajor.first().first())
        // ...while a cadence that would need a lower octave moves up one instead, into the melody's range.
        val dMajor = TonalContext.cadenceChords(rootMidi = 62, melodyLowest = 50)
        assertEquals(50, dMajor.first().first())
        assertTrue(dMajor.flatten().all { it >= Config.LOWEST_SAMPLED_NOTE })
    }

    @Test
    fun `nor above it, for a melody high in the range`() {
        // B major under a melody starting on C5: the highest B below C5 is B4 (71), whose V chord
        // reaches C#6 (85), above the last sample (C6 = 84) — so the cadence drops to B3.
        val chords = TonalContext.cadenceChords(rootMidi = 71, melodyLowest = 72)
        assertTrue(chords.toString(), chords.flatten().all { it in Config.LOWEST_SAMPLED_NOTE..Config.HIGHEST_SAMPLED_NOTE })
        assertEquals(59, chords.first().first())
    }

    @Test
    fun `every cadence note has a piano sample`() {
        for ((_, root) in keys) {
            for (note in TonalContext.cadenceChords(rootMidi = root, melodyLowest = root).flatten()) {
                assertTrue(MidiNote.name(note), note in Config.LOWEST_SAMPLED_NOTE..Config.HIGHEST_SAMPLED_NOTE)
            }
        }
    }

    @Test
    fun `scheduled, the cadence is four chords of one beat each`() {
        val chords = TonalContext.cadenceChords(rootMidi = 60, melodyLowest = 60)
        val timed = MelodyTiming.scheduleChords(chords, tempoBpm = 120) // beat = 0.5 s
        assertEquals(12, timed.size)
        assertEquals(listOf(0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 1.0, 1.0, 1.0, 1.5, 1.5, 1.5), timed.map { it.startSeconds })
        for (note in timed) assertEquals(0.5 * Config.MELODY_NOTE_GATE, note.durationSeconds, delta)
        assertEquals(chords.flatten(), timed.map { it.note })
    }
}
