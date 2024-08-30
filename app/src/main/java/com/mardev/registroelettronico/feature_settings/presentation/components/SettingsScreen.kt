package com.mardev.registroelettronico.feature_settings.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.mardev.registroelettronico.feature_settings.presentation.AppTheme
import com.mardev.registroelettronico.feature_settings.presentation.AppTheme.Companion.resourceIdStringFromAppTheme
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
                userSettings.theme = when (selectedOption) {
                    0 -> AppTheme.MODE_AUTO
                    1 -> AppTheme.MODE_DAY
                    2 -> AppTheme.MODE_NIGHT
                    else -> AppTheme.MODE_AUTO
                }
            }
        )
    }
    Scaffold { paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .consumeWindowInsets(paddingValues).then(
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                    } else Modifier),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            SettingItem(
                icon = Icons.Default.DarkMode,
                title = "Modalità tema",
                description = stringResource(
                    id = resourceIdStringFromAppTheme(userSettings.theme)
                )
            ) {
                showDialog = true
            }

            SwitchItem(
                icon = Icons.Default.Palette,
                title = "Colori dinamici",
                description = "Segli se attivare i colori dinamici (Android 12+)",
                initialValue = userSettings.dynamicColor,
            ) { value ->
                userSettings.dynamicColor = value
            }
        }
    }
}
