package com.mardev.registroelettronico.feature_home.presentation.components.lesson_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mardev.registroelettronico.feature_home.domain.model.Lesson
import com.mardev.registroelettronico.feature_home.presentation.components.common.DateItem

@Composable
fun LessonItem(
    lesson: Lesson,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = lesson.subject, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = lesson.description)
        Spacer(modifier = Modifier.height(4.dp))
        DateItem(date = lesson.date)
        Spacer(modifier = Modifier.height(8.dp))
    }
}