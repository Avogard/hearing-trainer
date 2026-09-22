package com.hearingtrainer.app.core

import kotlin.random.Random

/**
 * Generates melodies for the ear-training exercise. A pure function of [MelodySpec]: the same
 * spec (including its seed) always produces the same [Melody] (see docs/SPEC.md).
 */
object MelodyGenerator {

    fun generate(spec: MelodySpec): Melody {
        val scaleNotesInRange = (spec.lowestNote..spec.highestNote)
            .filter { spec.scale.contains(it, spec.rootNote) }
        require(scaleNotesInRange.isNotEmpty()) {
            "No notes of ${spec.scale} rooted at ${spec.rootNote} fall within " +
                "${spec.lowestNote}..${spec.highestNote}"
        }

        val random = Random(spec.seed)
        val notes = mutableListOf(firstNote(spec, scaleNotesInRange, random))

        while (notes.size < spec.length) {
            notes += nextNote(spec, scaleNotesInRange, previous = notes.last(), random)
        }

        return Melody(notes)
    }

    /** The first note: the root, or (when allowed) the root or the scale's fifth above it. */
    private fun firstNote(spec: MelodySpec, scaleNotesInRange: List<Int>, random: Random): Int {
        val rootChoices = scaleNotesInRange.filter { sameChroma(it, spec.rootNote) }
        val fifthChoices = scaleNotesInRange.filter { sameChroma(it, spec.scale.fifthAbove(spec.rootNote)) }
        val choices = if (spec.startOnRootOnly) rootChoices else rootChoices + fifthChoices
        check(choices.isNotEmpty()) {
            "Neither the root ${spec.rootNote} nor its fifth has an in-range occurrence within " +
                "${spec.lowestNote}..${spec.highestNote}"
        }
        return choices.random(random)
    }

    /** The next note: an in-range, in-scale note within [MelodySpec.maxInterval] of [previous]. */
    private fun nextNote(spec: MelodySpec, scaleNotesInRange: List<Int>, previous: Int, random: Random): Int {
        val minInterval = if (spec.allowImmediateRepeats) 0 else 1
        val strictCandidates = candidatesWithin(scaleNotesInRange, previous, minInterval, spec.maxInterval)
        val candidates = strictCandidates.ifEmpty {
            // Stuck near a range boundary: relax the no-repeat rule before giving up, rather
            // than silently producing a shorter melody than requested.
            candidatesWithin(scaleNotesInRange, previous, minInterval = 0, spec.maxInterval)
        }
        check(candidates.isNotEmpty()) {
            "Cannot extend melody from note $previous: no in-range, in-scale note is within " +
                "${spec.maxInterval} semitones. Widen the range or maxInterval."
        }
        return candidates.random(random)
    }

    private fun candidatesWithin(notes: List<Int>, from: Int, minInterval: Int, maxInterval: Int): List<Int> =
        notes.filter { Math.abs(it - from) in minInterval..maxInterval }

    private fun sameChroma(note: Int, reference: Int): Boolean = Math.floorMod(note - reference, 12) == 0
}
