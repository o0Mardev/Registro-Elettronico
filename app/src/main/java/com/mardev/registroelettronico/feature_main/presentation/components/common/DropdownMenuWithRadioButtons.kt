package com.mardev.registroelettronico.feature_main.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuWithRadioButtons(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        offset = DpOffset((-15).dp, 0.dp),
        onDismissRequest = onDismissRequest
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onOptionSelected(option)
                    onDismissRequest()
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = null // The parent DropdownMenuItem handles the click
                        )
                        Text(text = option, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            )
        }
    }
}