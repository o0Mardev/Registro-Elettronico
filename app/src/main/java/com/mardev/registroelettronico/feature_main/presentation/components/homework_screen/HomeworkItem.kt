package com.mardev.registroelettronico.feature_main.presentation.components.homework_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Assignment
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@Composable
fun HomeworkItem(
    homework: Homework,
    modifier: Modifier = Modifier,
    showSubject: Boolean = true,
    showDate: Boolean = true,
    showOverline: Boolean = false,
    showIcon: Boolean = false,
    showDivider: Boolean = false,
    onCheckedChange: ((id: Int, state: Boolean) -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        if (showIcon) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Green.copy(alpha = 0.75f, green = 0.5f),
                            radius = this.size.maxDimension / 1.3f
                        )
                    },
                imageVector = Icons.TwoTone.Assignment, tint = Color.White, contentDescription = null,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            if (showOverline) {
                Text(text = "Compito", style = MaterialTheme.typography.labelMedium)
            }
            if (showSubject) {
                Text(text = homework.subject, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(2.dp))
            HtmlText(text = homework.description, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground))
            Spacer(modifier = Modifier.height(4.dp))

            if (showDate) {
                DateItem(date = homework.dueDate)
            }
        }
        if (onCheckedChange != null) {
            Checkbox(checked = homework.completed, onCheckedChange = { state ->
                onCheckedChange(homework.id, state)
            })
        }
    }
    if (showDivider) {
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}