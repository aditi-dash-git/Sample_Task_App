package com.example.sampletaskapp

import androidx.compose.runtime.*
import androidx.compose.material3.Text

@Composable
fun AppNavigation() {

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Start) }

    when (currentScreen) {

        is Screen.Start -> StartScreen(
            onStartClick = { currentScreen = Screen.NoiseTest }
        )

        is Screen.NoiseTest -> NoiseTestScreen(
            onTestPass = { currentScreen = Screen.TaskSelection }
        )

        is Screen.TaskSelection -> TaskSelectionScreen(
            onTextReadingClick = { currentScreen = Screen.TextReading },
            onImageDescriptionClick = { currentScreen = Screen.ImageDescription },
            onPhotoCaptureClick = { currentScreen = Screen.PhotoCapture },
            onViewHistoryClick = { currentScreen = Screen.TaskHistory }
        )

        is Screen.TextReading -> TextReadingScreen(
            onBack = { currentScreen = Screen.TaskSelection }
        )

        is Screen.ImageDescription -> ImageDescriptionScreen(
            onBack = { currentScreen = Screen.TaskSelection }
        )

        is Screen.PhotoCapture -> PhotoCaptureScreen(
            onBack = { currentScreen = Screen.TaskSelection }
        )

        is Screen.TaskHistory -> TaskHistoryScreen(
            onBack = { currentScreen = Screen.TaskSelection }
        )
    }
}
