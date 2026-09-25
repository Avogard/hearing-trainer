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
     * no mode toggle, exactly as before scored mode existed.
     */
    const val SCORED_ANSWER_MODE = true

    /**
     * Dictation variant of scored mode: keys make no sound while answering (the reveal and the
     * final replay still sound). Wired through, no UI for it yet.
     */
    const val SILENT_SCORED_KEYBOARD = false
}
