package com.hearingtrainer.app.data

import com.hearingtrainer.app.core.Clock

/** The device's real clocks: wall time for timestamps, the JVM's monotonic timer for durations. */
object RealClock : Clock {
    override fun nowMillis(): Long = System.currentTimeMillis()
    override fun elapsedMillis(): Long = System.nanoTime() / 1_000_000L
}
