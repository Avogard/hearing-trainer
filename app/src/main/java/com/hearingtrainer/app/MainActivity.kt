package com.hearingtrainer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hearingtrainer.app.ui.home.HomeScreen
import com.hearingtrainer.app.ui.practice.PracticeScreen
import com.hearingtrainer.app.ui.settings.SettingsScreen
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

private enum class Screen { HOME, PRACTICE, SETTINGS }

/**
 * Switches between the three screens. Still no navigation library — Home is the hub and the
 * other two only ever go back to it, so one [rememberSaveable]d enum (it survives rotation) plus
 * a [BackHandler] that returns to Home is all the back stack there is (see docs/DECISIONS.md).
 */
@Composable
private fun AppRoot(modifier: Modifier = Modifier) {
    var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
    BackHandler(enabled = screen != Screen.HOME) { screen = Screen.HOME }

    when (screen) {
        Screen.HOME -> HomeScreen(
            onPracticeClicked = { screen = Screen.PRACTICE },
            onSettingsClicked = { screen = Screen.SETTINGS },
            modifier = modifier,
        )
        Screen.PRACTICE -> PracticeScreen(onDone = { screen = Screen.HOME }, modifier = modifier)
        Screen.SETTINGS -> SettingsScreen(onDone = { screen = Screen.HOME }, modifier = modifier)
    }
}
