package com.mardev.registroelettronico.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdateDialog() {
    val dialogViewModel: UpdateDialogViewModel = hiltViewModel()
    val dialogState by dialogViewModel.state

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (dialogState.showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                if (dialogState.showButtons) {
                    TextButton(onClick = {
                        dialogViewModel.changeUpdateDialogButtonsVisibility(false)
                        scope.launch {
                            dialogViewModel.updateApp(context)
                        }

                    }) {
                        Text(text = "Aggiorna")
                    }
                }
            },
            dismissButton = {
                if (dialogState.showButtons) {
                    TextButton(onClick = { dialogViewModel.changeUpdateDialogVisibility(false) }) {
                        Text(text = "Ignora")
                    }
                }
            },
            title = {
                Text(text = "Nuovo aggiornamento disponibile!")
            },
            text = {
                Box {
                    Text(text = "Un nuovo aggiornamento è disponibile al download. \nScaricando l'ultimo aggiornamento avrai le ultime funzionalità, migliorie e bug fix per Calcola sconto.")
                    if (dialogState.showProgress) {
                        LinearProgressIndicator(
                            progress = dialogState.progress,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(top = 120.dp)
                        )
                    }

                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = null
                )
            }
        )
    }
}