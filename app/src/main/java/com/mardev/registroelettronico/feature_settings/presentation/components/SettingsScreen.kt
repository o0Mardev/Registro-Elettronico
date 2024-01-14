package com.mardev.registroelettronico.feature_settings.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mardev.registroelettronico.feature_settings.presentation.AppTheme
import com.mardev.registroelettronico.feature_settings.presentation.UserSettings

@Composable
fun SettingsScreen(userSettings: UserSettings) {
    val darkModeOptions: List<String> = listOf("Segui sistema", "Chiara", "Scura")
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        AlertDialogWithRadioButtons(
            title = "Modalità tema",
            options = darkModeOptions,
            indexSelectedOption = userSettings.theme.ordinal,
            onDismiss = { showDialog = false },
            onConfirm = { selectedOption ->
                showDialog = false
                Log.d("TAG", "SettingsScreen: selectedOption $selectedOption")
                userSettings.theme = when(selectedOption){
                    darkModeOptions[0] -> AppTheme.MODE_AUTO
                    darkModeOptions[1] -> AppTheme.MODE_DAY
                    darkModeOptions[2] -> AppTheme.MODE_NIGHT
                    else -> AppTheme.MODE_AUTO
                }
            }
        )
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Button(onClick = { showDialog = true }) {
                Text(text = "Mostra opzioni")
            }

        }
    }


}