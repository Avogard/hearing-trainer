package com.hearingtrainer.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hearingtrainer.app.core.Config

/**
 * The Settings screen: just tempo and melody length, as big +/- steppers that work one-handed.
 * Changes apply to the next melody and are saved as you tap (see [SettingsViewModel]).
 */
@Composable
fun SettingsScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val viewModel: SettingsViewModel = viewModel(factory = remember { SettingsViewModelFactory(appContext) })
    val settings by viewModel.settings.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onDone) {
            Text(text = "← Home")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        StepperRow(
            label = "Tempo",
            hint = "One note per beat",
            value = "${settings.tempoBpm} BPM",
            canDecrease = settings.tempoBpm > Config.MIN_TEMPO_BPM,
            canIncrease = settings.tempoBpm < Config.MAX_TEMPO_BPM,
            onDecrease = { viewModel.onTempoStep(-1) },
            onIncrease = { viewModel.onTempoStep(+1) },
        )
        Spacer(modifier = Modifier.height(32.dp))
        StepperRow(
            label = "Melody length",
            hint = "Notes per melody",
            value = "${settings.melodyLength} notes",
            canDecrease = settings.melodyLength > Config.MIN_MELODY_LENGTH,
            canIncrease = settings.melodyLength < Config.MAX_MELODY_LENGTH,
            onDecrease = { viewModel.onLengthStep(-1) },
            onIncrease = { viewModel.onLengthStep(+1) },
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextButton(onClick = viewModel::onResetClicked) {
            Text(text = "Reset to defaults (${Config.DEFAULT_TEMPO_BPM} BPM, ${Config.DEFAULT_MELODY_LENGTH} notes)")
        }
    }
}

@Composable
private fun StepperRow(
    label: String,
    hint: String,
    value: String,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(
            text = hint,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StepButton(text = "−", enabled = canDecrease, onClick = onDecrease)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
            StepButton(text = "+", enabled = canIncrease, onClick = onIncrease)
        }
    }
}

@Composable
private fun StepButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(64.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}
