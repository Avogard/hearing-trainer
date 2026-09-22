package com.hearingtrainer.app.core

/**
 * Parameters for [MelodyGenerator]. All notes are MIDI numbers (60 = middle C).
 *
 * @param rootNote the key's root note.
 * @param scale the scale to draw notes from.
 * @param lowestNote lowest MIDI note the melody may use (inclusive).
 * @param highestNote highest MIDI note the melody may use (inclusive).
 * @param length how many notes the melody has.
 * @param maxInterval largest allowed interval between consecutive notes, in semitones.
 * @param startOnRootOnly if true the first note is always [rootNote]; if false it may also
 *   start on the scale's fifth above the root. Early difficulty levels use true — see
 *   docs/SPEC.md's "Melody generator" section.
 * @param allowImmediateRepeats if false (early levels), the same note may not sound twice in a
 *   row.
 * @param seed random seed. The same seed with the same other parameters always produces the
 *   same melody, so bugs are reproducible and tests are stable (see docs/SPEC.md).
 */
data class MelodySpec(
    val rootNote: Int,
    val scale: Scale,
    val lowestNote: Int,
    val highestNote: Int,
    val length: Int,
    val maxInterval: Int,
    val startOnRootOnly: Boolean = true,
    val allowImmediateRepeats: Boolean = false,
    val seed: Long,
) {
    init {
        require(lowestNote < highestNote) {
            "lowestNote ($lowestNote) must be less than highestNote ($highestNote)"
        }
        require(length >= 1) { "length must be at least 1, was $length" }
        require(maxInterval >= 1) { "maxInterval must be at least 1, was $maxInterval" }
    }
}
