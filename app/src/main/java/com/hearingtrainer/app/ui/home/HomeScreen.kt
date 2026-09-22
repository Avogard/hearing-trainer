package com.hearingtrainer.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * The Home screen from docs/SPEC.md: streak, today's status, one big Practice button, a small
 * settings icon. Streak / "done today" need persistence (data/, not built yet), so those are
 * just placeholder text for now — wiring them up is follow-up work, and so is the settings icon
 * (there's nothing to configure yet since Guided mode has no options, per docs/DECISIONS.md's
 * "good defaults over customisation").
 */
@Composable
fun HomeScreen(onPracticeClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Hearing Trainer", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Train your ear, one melody at a time.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onPracticeClicked, modifier = Modifier.height(64.dp)) {
            Text(text = "Practice", style = MaterialTheme.typography.titleLarge)
        }
    }
}
