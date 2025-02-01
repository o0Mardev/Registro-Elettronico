package com.mardev.registroelettronico.feature_settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SwitchItem(
    enabled: Boolean = true,
    icon: ImageVector,
    title: String,
    description: String,
    initialValue: Boolean,
    onCheckedChange: (value: Boolean) -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(initialValue) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(70.dp, 96.dp)
            .padding(8.dp)
            .then(
                if (enabled) Modifier.clickable(role = Role.Switch) {
                    checked = !checked
                    onCheckedChange(checked)
                } else Modifier
            )
            .alpha(if (enabled) 1f else 0.5f) // Reduce opacity when disabled
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(
            checked = checked,
            onCheckedChange = if (enabled) { value ->
                checked = value
                onCheckedChange(value)
            } else null, // Disable onCheckedChange when disabled
            enabled = enabled // Disable Switch when necessary
        )
    }
}
