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

    // --- scored answer mode (see docs/SPEC.md, "Answer modes", and core/ScoredAnswer.kt) ---

    /** How many times the melody may be replayed before the first key press of a scored answer. */
    const val MAX_REPLAYS_BEFORE_FIRST_PRESS = 2

    /** Wrong presses allowed at one position before the app reveals the note and moves on. */
    const val MAX_WRONG_PRESSES_PER_NOTE = 2

    /**
     * On a reveal, the pressed (wrong) note sounds first; the correct note and its key highlight
     * follow this much later so the two don't clash.
     */
    const val REVEAL_DELAY_MILLIS = 300L

    /** How long the correct key stays lit when a note is revealed. */
    const val REVEAL_HIGHLIGHT_MILLIS = 400L

    /**
     * After the last note of a scored melody that wasn't all first try, this much silence before
     * the melody plays once more, so the user's last note isn't cut off (starting a sequence fades
     * whatever is still sounding).
     */
    const val REPLAY_AFTER_FINISH_GAP_BEATS = 1.0

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
