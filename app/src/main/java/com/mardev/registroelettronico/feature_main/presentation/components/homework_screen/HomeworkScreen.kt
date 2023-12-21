package com.mardev.registroelettronico.feature_home.presentation.components.homework_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_home.presentation.HomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    state: HomeworkScreenState,
    onCheckedChange: (id: Int,state: Boolean) -> Unit
) {
    Scaffold { paddingValues ->
        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyColumn(Modifier.padding(paddingValues)) {
            items(state.homework.size) { i ->
                val homework = state.homework[i]
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                HomeworkItem(homework = homework){ id, state ->
                    onCheckedChange(id, state)
                }
                if (i < state.homework.size - 1) {
                    Divider()
                }
            }
        }
    }
}
