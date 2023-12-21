package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MenuBook
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@Composable
fun LessonItem(
    lesson: Lesson,
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
                        color = Color.Blue.copy(alpha = 0.75f),
                        radius = this.size.maxDimension / 1.3f
                    )
                }, imageVector = Icons.TwoTone.MenuBook, tint = Color.White, contentDescription = null)
        }
        Column(modifier = modifier) {
            if (showOverline) {
                Text(text = "Argomento", style = MaterialTheme.typography.labelMedium)
            }
            Text(text = lesson.subject, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = lesson.description, style = MaterialTheme.typography.bodyLarge)
            if (showDate) {
                DateItem(date = lesson.date)
            }
        }
    }
    if (showDivider) {
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}