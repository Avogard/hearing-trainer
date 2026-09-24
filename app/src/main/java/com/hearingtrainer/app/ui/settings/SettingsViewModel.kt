package com.hearingtrainer.app.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hearingtrainer.app.core.Config
import com.hearingtrainer.app.core.PracticeSettings
import com.hearingtrainer.app.core.SettingsStore
import com.hearingtrainer.app.data.SharedPreferencesSettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Drives the Settings screen. Every change is clamped to its allowed range and saved
 * immediately — there is no "Save" button to forget.
 */
class SettingsViewModel(private val store: SettingsStore) : ViewModel() {

    private val _settings = MutableStateFlow(store.load())
    val settings: StateFlow<PracticeSettings> = _settings.asStateFlow()

    /** [direction] is +1 or -1: one tap of the + or - button. */
    fun onTempoStep(direction: Int) = update { it.copy(tempoBpm = it.tempoBpm + direction * Config.TEMPO_STEP_BPM) }

    fun onLengthStep(direction: Int) = update { it.copy(melodyLength = it.melodyLength + direction) }

    fun onResetClicked() = update { PracticeSettings.DEFAULT }

    private fun update(change: (PracticeSettings) -> PracticeSettings) {
        val next = change(_settings.value).clamped()
        _settings.value = next
        store.save(next)
    }
}

/** The store needs a Context to construct, so `viewModel()` needs this factory. */
class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(SharedPreferencesSettingsStore(context)) as T
    }
}
