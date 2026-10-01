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
import androidx.compose.material3.HorizontalDivider
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
import com.hearingtrainer.app.ui.components.BackIcon
import com.hearingtrainer.app.ui.components.IconSquareButton
import com.hearingtrainer.app.ui.components.MinusIcon
import com.hearingtrainer.app.ui.components.PlusIcon
import com.hearingtrainer.app.ui.components.StepperButton
import com.hearingtrainer.app.ui.components.SurfaceCard

/**
 * The Settings screen: just tempo and melody length, as big +/- steppers that work one-handed.
 * Changes apply to the next melody and are saved as you tap (see [SettingsViewModel]).
 */
@Composable
fun SettingsScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val viewModel: SettingsViewModel = viewModel(factory = remember { SettingsViewModelFactory(appContext) })
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp, bottom = 28.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconSquareButton(onClick = onDone, contentDescription = "Back to Home") { BackIcon() }
            Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(28.dp))

        SurfaceCard(padding = PaddingValues(horizontal = 20.dp)) {
            StepperRow(
                label = "Tempo",
                hint = "One note per beat",
                value = "${settings.tempoBpm} BPM",
                canDecrease = settings.tempoBpm > Config.MIN_TEMPO_BPM,
                canIncrease = settings.tempoBpm < Config.MAX_TEMPO_BPM,
                decreaseLabel = "Slower",
                increaseLabel = "Faster",
                onDecrease = { viewModel.onTempoStep(-1) },
                onIncrease = { viewModel.onTempoStep(+1) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            StepperRow(
                label = "Melody length",
                hint = "Notes per melody",
                value = "${settings.melodyLength} notes",
                canDecrease = settings.melodyLength > Config.MIN_MELODY_LENGTH,
                canIncrease = settings.melodyLength < Config.MAX_MELODY_LENGTH,
                decreaseLabel = "Shorter",
                increaseLabel = "Longer",
                onDecrease = { viewModel.onLengthStep(-1) },
                onIncrease = { viewModel.onLengthStep(+1) },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = viewModel::onResetClicked, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text(
                text = "Reset to defaults · ${Config.DEFAULT_TEMPO_BPM} BPM, ${Config.DEFAULT_MELODY_LENGTH} notes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
    decreaseLabel: String,
    increaseLabel: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(
            text = hint,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StepperButton(onClick = onDecrease, contentDescription = decreaseLabel, enabled = canDecrease) { MinusIcon() }
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            StepperButton(onClick = onIncrease, contentDescription = increaseLabel, enabled = canIncrease) { PlusIcon() }
        }
    }
}
