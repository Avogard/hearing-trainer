package com.hearingtrainer.app.data

import android.content.Context
import android.content.SharedPreferences
import com.hearingtrainer.app.core.PracticeSettings
import com.hearingtrainer.app.core.SettingsStore

/**
 * Keeps [PracticeSettings] in SharedPreferences — Android's built-in key/value file, the plain
 * choice for two integers (see docs/DECISIONS.md, "SharedPreferences for settings, not DataStore
 * yet"). Values are clamped on the way in and out, so a stale or hand-edited file can't put the
 * app into an out-of-range state.
 */
class SharedPreferencesSettingsStore(context: Context) : SettingsStore {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    override fun load(): PracticeSettings = PracticeSettings(
        tempoBpm = prefs.getInt(KEY_TEMPO_BPM, PracticeSettings.DEFAULT.tempoBpm),
        melodyLength = prefs.getInt(KEY_MELODY_LENGTH, PracticeSettings.DEFAULT.melodyLength),
    ).clamped()

    override fun save(settings: PracticeSettings) {
        val clamped = settings.clamped()
        prefs.edit()
            .putInt(KEY_TEMPO_BPM, clamped.tempoBpm)
            .putInt(KEY_MELODY_LENGTH, clamped.melodyLength)
            .apply() // async write to disk; the in-memory value is updated immediately
    }

    private companion object {
        const val FILE_NAME = "practice_settings"
        const val KEY_TEMPO_BPM = "tempo_bpm"
        const val KEY_MELODY_LENGTH = "melody_length"
    }
}
