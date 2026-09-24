package com.hearingtrainer.app.core

/**
 * The Guided-mode difficulty ladder from docs/SPEC.md. Only Level 1 is implemented so far —
 * levels 2-5 and the 80%-over-20-melodies promotion rule need progress tracking (data/, not
 * built yet) to mean anything, so they're follow-up work, not stubbed out here.
 */
object DifficultyLevel {

    /**
     * Level 1: C major, range C4-G4, always starts on the root, max interval 4 semitones (steps
     * and thirds — see the "Level 1 was nearly the same melody every time" decision in
     * docs/DECISIONS.md for why this isn't 2). [length] comes from the user's settings; the
     * default is [Config.DEFAULT_MELODY_LENGTH].
     */
    fun level1(seed: Long, length: Int = Config.DEFAULT_MELODY_LENGTH): MelodySpec = MelodySpec(
        rootNote = 60, // C4
        scale = Scale.MAJOR,
        lowestNote = 60, // C4
        highestNote = 67, // G4
        length = length,
        maxInterval = 4,
        startOnRootOnly = true,
        allowImmediateRepeats = false,
        seed = seed,
    )
}
