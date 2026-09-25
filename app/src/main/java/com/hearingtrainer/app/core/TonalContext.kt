package com.hearingtrainer.app.core

/**
 * The key context that sounds before a melody, so its notes have a key to belong to (docs/PLAN.md,
 * "Key context"; docs/DECISIONS.md, "A cadence before each melody"). Major keys only for now.
 */
object TonalContext {

    /** Scale degrees (semitones above the root) of the I, IV, V, I chord roots in a major key. */
    private val CADENCE_ROOT_OFFSETS = listOf(0, 5, 7, 0)

    /** A major triad: root, major third, perfect fifth. */
    private val MAJOR_TRIAD = listOf(0, 4, 7)

    /**
     * A I–IV–V–I cadence in the major key rooted at [rootMidi], as four root-position triads
     * (three MIDI notes each), one chord per beat when scheduled with [MelodyTiming.scheduleChords].
     *
     * The register sits just below the melody: the tonic root is the highest note of the root's
     * pitch class strictly below [melodyLowest], and the IV and V chords are built up from the roots
     * in that same octave (C major under a C4 melody: C3-E3-G3, F3-A3-C4, G3-B3-D4, C3-E3-G3). The
     * tonic chord is therefore entirely below the melody; IV and V may touch its bottom notes,
     * which root-position triads can't avoid. If a chord tone would fall outside the sampled range
     * (a very low or very high melody), the cadence moves an octave in rather than going silent.
     */
    fun cadenceChords(rootMidi: Int, melodyLowest: Int): List<List<Int>> {
        var tonicRoot = melodyLowest - 1 - Math.floorMod(melodyLowest - 1 - rootMidi, 12)
        if (tonicRoot < Config.LOWEST_SAMPLED_NOTE) tonicRoot += 12
        if (tonicRoot + HIGHEST_CHORD_TONE_OFFSET > Config.HIGHEST_SAMPLED_NOTE) tonicRoot -= 12
        return CADENCE_ROOT_OFFSETS.map { degree -> MAJOR_TRIAD.map { tonicRoot + degree + it } }
    }

    /** The V chord's fifth, the cadence's highest note, relative to the tonic root. */
    private val HIGHEST_CHORD_TONE_OFFSET = CADENCE_ROOT_OFFSETS.max() + MAJOR_TRIAD.max()
}
