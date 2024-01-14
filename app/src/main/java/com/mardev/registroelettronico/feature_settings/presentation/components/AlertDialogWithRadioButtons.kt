package com.mardev.registroelettronico.feature_settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogWithRadioButtons(
    title: String,
    options: List<String>,
    indexSelectedOption: Int,
    onDismiss: () -> Unit,
    onConfirm: (selectedOption: String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(options[indexSelectedOption]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(selectedOption)
            }) {
                Text(text = "Applica")
            }
        },
        title = { Text(text = title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = selectedOption == option,
                                onClick = {
                                    selectedOption = option
                                },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(selected = option == selectedOption, onClick = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = option)
                    }
                }
            }
        }
    )
}

