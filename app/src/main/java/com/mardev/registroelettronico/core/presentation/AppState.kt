package com.mardev.registroelettronico.core.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class AppState(
    val snackbarHostState: SnackbarHostState,
    val snackbarScope: CoroutineScope,
    var isDarkMode: MutableState<Boolean>
) {
    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }

}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    isDarkMode: Boolean = isSystemInDarkTheme()
): AppState {
    val darkModeState by remember { mutableStateOf(isDarkMode) }

    return remember(snackbarHostState, snackbarScope, darkModeState) {
        AppState(
            snackbarHostState = snackbarHostState,
            snackbarScope = snackbarScope,
            isDarkMode = mutableStateOf(darkModeState)
        )
    }
}
