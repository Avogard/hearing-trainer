package com.hearingtrainer.app.core

/**
 * The Guided-mode difficulty ladder from docs/SPEC.md. Only Level 1 is implemented so far —
 * levels 2-5 and the 80%-over-20-melodies promotion rule need progress tracking (data/, not
 * built yet) to mean anything, so they're follow-up work, not stubbed out here.
 */
object DifficultyLevel {

    /** Level 1: C major, 3 notes, range C4-G4, steps only, always starts on the root. */
    fun level1(seed: Long): MelodySpec = MelodySpec(
        rootNote = 60, // C4
        scale = Scale.MAJOR,
        lowestNote = 60, // C4
        highestNote = 67, // G4
        length = 3,
        maxInterval = 2,
        startOnRootOnly = true,
        allowImmediateRepeats = false,
        seed = seed,
    )
}
