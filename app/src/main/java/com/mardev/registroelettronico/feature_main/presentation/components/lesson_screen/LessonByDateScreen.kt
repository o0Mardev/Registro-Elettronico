package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonByDateScreen(
    modifier: Modifier,
    groupedLessons: Map<LocalDate, List<Lesson>>
) {
    LazyColumn(modifier = modifier.then(
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .windowInsetsPadding(WindowInsets.displayCutout)
        } else modifier)) {
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
                HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
            )
        }
    }
}