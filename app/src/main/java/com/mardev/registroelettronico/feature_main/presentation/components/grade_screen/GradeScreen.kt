package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GradeScreen(
    state: GradeScreenState,
) {


    Scaffold { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {

            val grouped = state.grades.groupBy { it.date }

            grouped.forEach { (header, items) ->
                stickyHeader {
                    DateItem(
                        date = header,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                items(items, key = { item -> item.id }) { i ->
                    Spacer(modifier = Modifier.height(4.dp))
                    GradeItem(grade = i, showDate = false)
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(thickness = DividerDefaults.Thickness.times(2))
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}