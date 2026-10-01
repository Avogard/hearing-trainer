package com.hearingtrainer.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hearingtrainer.app.data.SharedPreferencesSettingsStore
import com.hearingtrainer.app.ui.components.BigButtonHeight
import com.hearingtrainer.app.ui.components.Chip
import com.hearingtrainer.app.ui.components.Eyebrow
import com.hearingtrainer.app.ui.components.PrimaryButton
import com.hearingtrainer.app.ui.components.IconSquareButton
import com.hearingtrainer.app.ui.components.SlidersIcon
import com.hearingtrainer.app.ui.components.SurfaceCard

/**
 * The Home screen from docs/SPEC.md: the app name and a small settings button on top, a Today
 * card with the current level and settings, and one big Practice button under the thumb. The
 * streak count and the week strip from the design need persistence (data/, not built yet), so
 * they're not here yet rather than shown with made-up numbers.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onPracticeClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appContext = LocalContext.current.applicationContext
    // Home leaves the composition while Settings is open, so this re-reads on every return and a
    // changed tempo or length shows up right away. Two ints from SharedPreferences: no ViewModel.
    val settings = remember { SharedPreferencesSettingsStore(appContext).load() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp, bottom = 28.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Hearing Trainer", style = MaterialTheme.typography.titleLarge)
            IconSquareButton(onClick = onSettingsClicked, contentDescription = "Settings") { SlidersIcon() }
        }

        Spacer(modifier = Modifier.height(44.dp))
        Text(text = "Play what you hear.", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "A short melody, your keyboard, feedback on every note. A few minutes a day.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))
        SurfaceCard {
            Eyebrow(text = "Today")
            Text(
                text = "Ready when you are",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = "Level 1 · C major, five notes to choose from",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
            FlowRow(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Chip(text = "Level 1")
                Chip(text = "C major")
                Chip(text = "${settings.melodyLength} notes")
                Chip(text = "${settings.tempoBpm} BPM")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            text = "Practice",
            onClick = onPracticeClicked,
            height = BigButtonHeight,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
