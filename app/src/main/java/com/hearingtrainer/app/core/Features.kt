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
}
