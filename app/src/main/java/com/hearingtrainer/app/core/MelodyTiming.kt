package com.hearingtrainer.app.core

/**
 * One note of a scheduled sequence, in seconds from the start of the sequence: when it begins and
 * how long it sounds before it is released. The audio engine plays these sample-accurately, so
 * the rhythm is exact no matter what the UI thread is doing. [gain] scales the note's level
 * (1 = a normal melody note); chord tones use less so a chord doesn't drown the melody.
 */
data class TimedNote(val note: Int, val startSeconds: Double, val durationSeconds: Double, val gain: Float = 1f)

/**
 * Turns notes into a timed sequence: one note (or one chord) per beat at a given tempo. Chords are
 * simply several [TimedNote]s with the same start — the engine makes one voice per entry — so a
 * cadence and a melody go into one schedule and one [com.hearingtrainer.app.audio.NotePlayer.playSequence]
 * call, which keeps the gap between them sample-accurate.
 */
object MelodyTiming {

    fun beatSeconds(tempoBpm: Int): Double {
        require(tempoBpm > 0) { "tempoBpm must be positive, was $tempoBpm" }
        return 60.0 / tempoBpm
    }

    /**
     * Each note starts on its beat, the first at [startBeat] beats into the sequence, and sounds
     * for [Config.MELODY_NOTE_GATE] of the beat.
     */
    fun schedule(notes: List<Int>, tempoBpm: Int, startBeat: Double = 0.0): List<TimedNote> {
        val beat = beatSeconds(tempoBpm)
        return notes.mapIndexed { index, note ->
            TimedNote(note = note, startSeconds = (startBeat + index) * beat, durationSeconds = beat * Config.MELODY_NOTE_GATE)
        }
    }

    /**
     * One chord per beat, all of a chord's notes starting together, gated like melody notes, each
     * tone at [gain] (see [Config.CADENCE_GAIN] for why chord tones play quieter).
     */
    fun scheduleChords(chords: List<List<Int>>, tempoBpm: Int, startBeat: Double = 0.0, gain: Float = 1f): List<TimedNote> {
        val beat = beatSeconds(tempoBpm)
        return chords.flatMapIndexed { index, chord ->
            chord.map { note ->
                TimedNote(
                    note = note,
                    startSeconds = (startBeat + index) * beat,
                    durationSeconds = beat * Config.MELODY_NOTE_GATE,
                    gain = gain,
                )
            }
        }
    }

    /**
     * The whole prompt as one sequence: the [cadence] chords (one beat each, at
     * [Config.CADENCE_GAIN]), then [Config.CADENCE_GAP_BEATS] of silence, then the [melody]. An
     * empty cadence gives just the melody, starting at once.
     */
    fun scheduleWithCadence(cadence: List<List<Int>>, melody: List<Int>, tempoBpm: Int): List<TimedNote> {
        if (cadence.isEmpty()) return schedule(melody, tempoBpm)
        val melodyStartBeat = cadence.size + Config.CADENCE_GAP_BEATS
        return scheduleChords(cadence, tempoBpm, gain = Config.CADENCE_GAIN) +
            schedule(melody, tempoBpm, startBeat = melodyStartBeat)
    }

    /** [totalSeconds] for a plain melody of [noteCount] notes scheduled from beat 0. */
    fun totalSeconds(noteCount: Int, tempoBpm: Int): Double =
        noteCount * beatSeconds(tempoBpm) + Config.PAUSE_AFTER_PLAYBACK_SECONDS

    /**
     * How long the UI should wait after starting a sequence built here before treating playback
     * as finished: the last onset, its full beat (every event is one beat long) and a short pause,
     * so the last note's tail isn't cut off by whatever happens next.
     */
    fun totalSeconds(sequence: List<TimedNote>, tempoBpm: Int): Double {
        val lastOnset = sequence.maxOfOrNull { it.startSeconds } ?: -beatSeconds(tempoBpm)
        return lastOnset + beatSeconds(tempoBpm) + Config.PAUSE_AFTER_PLAYBACK_SECONDS
    }
}
