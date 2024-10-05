package com.mardev.registroelettronico.feature_main.presentation.components.about_screen

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
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mardev.registroelettronico.BuildConfig
import com.mardev.registroelettronico.core.presentation.SnackbarController
import com.mardev.registroelettronico.core.presentation.SnackbarEvent
import com.mardev.registroelettronico.core.presentation.components.UpdateDialog
import com.mardev.registroelettronico.core.presentation.components.UpdateDialogViewModel
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_settings.presentation.components.SettingItem
import kotlinx.coroutines.launch

@Composable
fun AboutScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .verticalScroll(rememberScrollState())
                .then(
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                    } else Modifier
                ),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            SettingItem(
                icon = Icons.Default.SystemUpdate,
                title = "Versione app",
                description = BuildConfig.VERSION_NAME
            )


            var isUpdateAvailable by rememberSaveable {
                mutableStateOf(false)
            }
            val dialogViewModel: UpdateDialogViewModel = hiltViewModel()
            val scope = rememberCoroutineScope()


            SettingItem(icon = Icons.Default.Update, title = "Cerca aggiornamenti", description = "Controlla manualmente gli aggiornamenti"){
                scope.launch {
                    isUpdateAvailable = dialogViewModel.isUpdateAvailable()
                    if (!isUpdateAvailable){
                        SnackbarController.sendEvent(SnackbarEvent(message = UIText.DynamicString(
                            "Nessun aggiornamento disponibile"
                        )))
                    }
                }
            }

            val dialogState by dialogViewModel.state.collectAsStateWithLifecycle()
            val context = LocalContext.current
            if (isUpdateAvailable){
                UpdateDialog(dialogState,
                    onUpdateClick = {
                        dialogViewModel.changeUpdateDialogButtonsVisibility(false)
                        scope.launch {
                            dialogViewModel.updateApp(context)
                        }
                    },
                    onIgnoreUpdateClick = { dialogViewModel.changeUpdateDialogVisibility(false) }
                )
            }


            val uriHandler = LocalUriHandler.current
            SettingItem(
                icon = Icons.Default.Feedback,
                title = "Feedback e richieste",
                description = "Questo è un progetto open-source"
            ) {
                uriHandler.openUri("https://github.com/o0Mardev/Registro-Elettronico")
            }
        }
    }
}
