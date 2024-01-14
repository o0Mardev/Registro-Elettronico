package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp

@Composable
fun GradeScreen(
    state: GradeScreenState,
) {
    val tabs = listOf("Giorni", "Materie")
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed{ index, tabTitle ->
                    Tab(selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }) {
                        Text(text = tabTitle)
                    }
                }
            }
        }
    ) { paddingValues ->
        when(selectedTabIndex){
            0 -> {
                val groupedGrades = state.grades.groupBy { it.date }
                GradeByDateScreen(modifier = Modifier.padding(paddingValues).padding(top = 8.dp), groupedGrades = groupedGrades)
            }
            1 -> {
                val groupedGrades = state.grades.groupBy { it.subject }
                GradeBySubjectScreen(modifier = Modifier.padding(paddingValues).padding(top = 8.dp), groupedGrades = groupedGrades)
            }
        }

        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}