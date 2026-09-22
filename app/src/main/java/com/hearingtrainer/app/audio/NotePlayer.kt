package com.hearingtrainer.app.audio

/**
 * Plays MIDI notes as sound. The only thing `ui/` knows about audio is this interface — the
 * implementation (SoundPool, see [SoundPoolNotePlayer]) lives entirely in this package, per
 * CLAUDE.md's architecture rules.
 */
interface NotePlayer {
    /** Plays [note] (a MIDI number, 60 = middle C) once, from the start. */
    fun play(note: Int)

    /** Releases the underlying audio resources. Call when the screen using this is done. */
    fun release()
}
