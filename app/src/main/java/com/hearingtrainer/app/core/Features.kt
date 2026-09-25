package com.hearingtrainer.app.core

/**
 * Feature flags: plain booleans so new behavior can be switched off without a revert
 * (CLAUDE.md, "Changeability"). Flip one, rebuild, done.
 */
object Features {

    /** The "My answer" button on the Practice screen: hear what you played before checking it. */
    const val HEAR_MY_ANSWER = true

    /** After a wrong answer is checked, play the correct melody once more automatically. */
    const val REPLAY_MELODY_AFTER_WRONG_ANSWER = true

    /**
     * Scored answers: every key press is final, limited replays, notes revealed after repeated
     * misses (docs/SPEC.md, "Answer modes"). When false the Practice screen is free play only, with
     * no mode toggle — the answer flow from before scored mode existed. The cadence and the attempt
     * log have their own flags below; all three off is "exactly as before".
     */
    const val SCORED_ANSWER_MODE = true

    /** Play a I-IV-V-I cadence in the melody's key before each melody, so the notes have a key to belong to. */
    const val CADENCE_BEFORE_MELODY = true

    /**
     * Dictation variant of scored mode: keys make no sound while answering (the reveal and the
     * final replay still sound). Wired through, no UI for it yet.
     */
    const val SILENT_SCORED_KEYBOARD = false

    /** Append one line per finished melody to the attempt log (data/FileAttemptLog.kt). */
    const val LOG_ATTEMPTS = true
}
