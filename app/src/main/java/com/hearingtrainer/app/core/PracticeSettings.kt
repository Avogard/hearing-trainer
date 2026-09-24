package com.hearingtrainer.app.core

/**
 * What the user can change on the Settings screen. Kept deliberately tiny (docs/DECISIONS.md:
 * "good defaults over customisation").
 *
 * @param tempoBpm playback tempo; a melody plays one note per beat.
 * @param melodyLength how many notes a generated melody has.
 */
data class PracticeSettings(
    val tempoBpm: Int = Config.DEFAULT_TEMPO_BPM,
    val melodyLength: Int = Config.DEFAULT_MELODY_LENGTH,
) {
    /** Returns a copy with every value forced into its allowed range (see [Config]). */
    fun clamped(): PracticeSettings = PracticeSettings(
        tempoBpm = tempoBpm.coerceIn(Config.MIN_TEMPO_BPM, Config.MAX_TEMPO_BPM),
        melodyLength = melodyLength.coerceIn(Config.MIN_MELODY_LENGTH, Config.MAX_MELODY_LENGTH),
    )

    companion object {
        val DEFAULT = PracticeSettings()
    }
}

/**
 * Where [PracticeSettings] are kept between app launches. `ui/` only ever sees this interface;
 * the Android implementation lives in `data/` (CLAUDE.md's boundary rule), and [InMemorySettingsStore]
 * exists for tests and previews.
 */
interface SettingsStore {
    fun load(): PracticeSettings
    fun save(settings: PracticeSettings)
}

class InMemorySettingsStore(initial: PracticeSettings = PracticeSettings.DEFAULT) : SettingsStore {
    private var current = initial.clamped()
    override fun load(): PracticeSettings = current
    override fun save(settings: PracticeSettings) {
        current = settings.clamped()
    }
}
