package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.mardev.registroelettronico.feature_main.domain.model.Note
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    showDate: Boolean = true,
    showOverline: Boolean = false,
    showIcon: Boolean = false,
    showDivider: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        if (showIcon) {
            Icon(modifier = Modifier
                .padding(12.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Magenta.copy(alpha = 0.75f),
                        radius = this.size.maxDimension / 1.3f
                    )
                }, imageVector = Icons.Default.NoteAlt, tint = Color.White, contentDescription = null)
        }
        Column(modifier = modifier) {
            if (showOverline) {
                Text(text = "Nota", style = MaterialTheme.typography.labelMedium)
            }
            when (note.type){
                'C' -> {
                    Text(text = "Tipo: Classe", style = MaterialTheme.typography.titleMedium)
                }
                else -> {
                    Text(text = "Tipo: ?", style = MaterialTheme.typography.titleMedium)
                }
            }
            Text(text = note.teacher, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(2.dp))
            HtmlText(text = note.description, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground))
            if (showDate) {
                DateItem(date = note.date)
            }
        }
    }
    if (showDivider) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}