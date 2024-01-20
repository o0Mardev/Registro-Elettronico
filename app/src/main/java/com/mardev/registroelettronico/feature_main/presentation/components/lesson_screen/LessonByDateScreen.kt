package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonItem
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonByDateScreen(
    modifier: Modifier,
    groupedLessons: Map<LocalDate, List<Lesson>>
) {
    LazyColumn(modifier = modifier) {
        groupedLessons.forEach { (header, items) ->
            stickyHeader {
                DateItem(
                    date = header,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(items, key = { item -> item.id }) { lesson ->
                Spacer(modifier = Modifier.height(4.dp))
                LessonItem(lesson = lesson, showDate = false)
                Spacer(modifier = Modifier.height(4.dp))
                Divider(thickness = DividerDefaults.Thickness.times(2))
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}