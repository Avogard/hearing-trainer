package com.hearingtrainer.app.core

/**
 * Every default setting and tuning constant in one place (CLAUDE.md: "never scattered as magic
 * numbers"). The difficulty ladder itself lives next door in [DifficultyLevel].
 *
 * Tempo is in beats per minute; a melody plays one note per beat.
 */
object Config {

    // --- user-adjustable settings (see PracticeSettings and the Settings screen) ---

    const val DEFAULT_TEMPO_BPM = 90
    const val MIN_TEMPO_BPM = 40
    const val MAX_TEMPO_BPM = 180
    /** How much one tap of the +/- buttons on the Settings screen changes the tempo. */
    const val TEMPO_STEP_BPM = 5

    const val DEFAULT_MELODY_LENGTH = 4
    const val MIN_MELODY_LENGTH = 2
    const val MAX_MELODY_LENGTH = 8

    // --- playback feel ---

    /**
     * Fraction of its beat a melody note sounds before it is released. Below 1.0 the notes are
     * cleanly separated (detached) instead of blurring into each other.
     */
    const val MELODY_NOTE_GATE = 0.85

    /** Silence kept after the last note of a played sequence before the UI moves on. */
    const val PAUSE_AFTER_PLAYBACK_SECONDS = 0.25

    // --- audio engine (audio/MixerNotePlayer) ---

    /** Fade-out applied when a note is released, so a note-off never clicks. */
    const val NOTE_RELEASE_SECONDS = 0.06

    /** Level of one voice in the mix. Two full-level notes at once just touch the soft clipper. */
    const val VOICE_GAIN = 0.7f

    /** Most simultaneous notes; the oldest is faded out when a new one would exceed this. */
    const val MAX_VOICES = 8

    /** Lowest and highest MIDI note the app has a piano sample for (C3..C6, res/raw/piano_*.wav). */
    const val LOWEST_SAMPLED_NOTE = 48
    const val HIGHEST_SAMPLED_NOTE = 84
}
