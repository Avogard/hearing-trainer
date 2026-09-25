package com.hearingtrainer.app.core

/**
 * Where "now" comes from. One of the four boundary interfaces named in CLAUDE.md: the ViewModel
 * asks this, never the platform, so tests can hand it fixed times. The real one is
 * `data/RealClock`; [ScoredAnswer] itself takes times as plain parameters and never reads a clock.
 */
interface Clock {

    /** Wall-clock time in milliseconds since the Unix epoch, for timestamps in logs. */
    fun nowMillis(): Long

    /**
     * A monotonic time in milliseconds for measuring durations (response times). It has no
     * meaningful zero and, unlike [nowMillis], never jumps when the phone's clock is adjusted.
     */
    fun elapsedMillis(): Long
}
