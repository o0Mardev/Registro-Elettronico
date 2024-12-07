package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

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
fun GradeScreen(
    state: GradeScreenState
) {
    val tabs = listOf("Giorni", "Materie")
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    Scaffold(topBar = {
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
            if (state.grades.isEmpty()) {
                Text(
                    text = "Non sono presenti voti per il seguente periodo.\n" +
                            "Ricorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            when (selectedTabIndex) {
                0 -> {
                    val groupedGrades = state.grades.groupBy { it.date }
                    GradeByDateScreen(
                        modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                        groupedGrades = groupedGrades
                    )
                }

                1 -> {
                    val groupedGrades = state.grades.groupBy { it.subject }
                    GradeBySubjectScreen(
                        modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                        groupedGrades = groupedGrades
                    )
                }
            }
        }
    }
}
