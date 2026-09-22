package com.hearingtrainer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hearingtrainer.app.ui.home.HomeScreen
import com.hearingtrainer.app.ui.practice.PracticeScreen
import com.hearingtrainer.app.ui.theme.HearingTrainerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearingTrainerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Switches between Home and Practice. Two screens in one Activity doesn't need a navigation
 * library yet — this is plain [remember]ed state. Navigation Compose is the natural next step
 * if more screens get added later (see docs/DECISIONS.md).
 */
@Composable
private fun AppRoot(modifier: Modifier = Modifier) {
    var showPractice by remember { mutableStateOf(false) }
    if (showPractice) {
        PracticeScreen(onDone = { showPractice = false }, modifier = modifier)
    } else {
        HomeScreen(onPracticeClicked = { showPractice = true }, modifier = modifier)
    }
}
