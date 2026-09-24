package com.hearingtrainer.app.audio

import com.hearingtrainer.app.core.TimedNote

/**
 * Plays MIDI notes as sound. The only thing `ui/` knows about audio is this interface — the
 * implementation ([MixerNotePlayer]) lives entirely in this package, per CLAUDE.md's
 * architecture rules, so it can be swapped (e.g. for an Oboe/NDK engine) without touching screens.
 *
 * All methods are safe to call from the main thread and return immediately.
 */
interface NotePlayer {

    /** Sounds [note] (a MIDI number, 60 = middle C) right now and lets it ring out naturally. */
    fun play(note: Int)

    /**
     * Plays a whole sequence with sample-accurate timing: each note starts at its
     * [TimedNote.startSeconds] and is released after its [TimedNote.durationSeconds]. Anything
     * already sounding is faded out first.
     */
    fun playSequence(sequence: List<TimedNote>)

    /** Fades out everything that is sounding or scheduled. */
    fun stop()

    /**
     * Whether the output stream should be running. Set false when the screen using the player
     * is hidden (stops audio and lets the device's audio hardware idle), true when it is shown.
     */
    fun setActive(active: Boolean)

    /** Releases the underlying audio resources. The player is unusable afterwards. */
    fun release()
}
