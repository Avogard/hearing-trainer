package com.hearingtrainer.app.core

/**
 * A musical scale, expressed as semitone offsets from its root within one octave.
 *
 * Only MAJOR and NATURAL_MINOR are implemented for v1 (see docs/SPEC.md's difficulty
 * ladder — pentatonic and chromatic are later levels, see docs/IDEAS.md).
 */
enum class Scale(val semitoneOffsets: List<Int>) {
    MAJOR(listOf(0, 2, 4, 5, 7, 9, 11)),
    NATURAL_MINOR(listOf(0, 2, 3, 5, 7, 8, 10));

    /** True if [note] (any MIDI note, any octave) belongs to this scale rooted at [rootNote]. */
    fun contains(note: Int, rootNote: Int): Boolean {
        val offsetFromRoot = Math.floorMod(note - rootNote, 12)
        return offsetFromRoot in semitoneOffsets
    }

    /** The scale's 5th degree above [rootNote], as a MIDI note (a perfect fifth in both scales). */
    fun fifthAbove(rootNote: Int): Int = rootNote + 7
}
