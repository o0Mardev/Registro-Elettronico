package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LessonScreen(
    state: LessonScreenState,
) {
    val tabs = listOf("Giorni", "Materie")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(4.dp))
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index }) {
                            Text(text = tabTitle)
                        }
                    }
                }
            }
            if (state.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            if (state.lessons.isEmpty()) {
                Text(
                    text = "Non sono presenti lezioni.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                when (selectedTabIndex) {
                    0 -> {
                        val groupedLessons = state.lessons.groupBy { it.date }
                        LessonByDateScreen(
                            modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                            groupedLessons = groupedLessons
                        )
                    }

                    1 -> {
                        val groupedLessons = state.lessons.groupBy { it.subject }
                        LessonBySubjectScreen(
                            modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                            groupedLessons = groupedLessons
                        )
                    }
                }
            }

        }
    }
}
