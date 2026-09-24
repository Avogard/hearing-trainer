package com.hearingtrainer.app.core

/**
 * One note of a scheduled sequence, in seconds from the start of the sequence: when it begins and
 * how long it sounds before it is released. The audio engine plays these sample-accurately, so
 * the rhythm is exact no matter what the UI thread is doing.
 */
data class TimedNote(val note: Int, val startSeconds: Double, val durationSeconds: Double)

/** Turns a list of notes into a timed sequence: one note per beat at a given tempo. */
object MelodyTiming {

    fun beatSeconds(tempoBpm: Int): Double {
        require(tempoBpm > 0) { "tempoBpm must be positive, was $tempoBpm" }
        return 60.0 / tempoBpm
    }

    /** Each note starts on its beat and sounds for [Config.MELODY_NOTE_GATE] of the beat. */
    fun schedule(notes: List<Int>, tempoBpm: Int): List<TimedNote> {
        val beat = beatSeconds(tempoBpm)
        return notes.mapIndexed { index, note ->
            TimedNote(note = note, startSeconds = index * beat, durationSeconds = beat * Config.MELODY_NOTE_GATE)
        }
    }

    /**
     * How long the UI should wait after starting [schedule]'s output before treating playback as
     * finished: the full last beat plus a short pause, so the last note's tail isn't cut off by
     * whatever happens next.
     */
    fun totalSeconds(noteCount: Int, tempoBpm: Int): Double =
        noteCount * beatSeconds(tempoBpm) + Config.PAUSE_AFTER_PLAYBACK_SECONDS
}
