package com.mardev.registroelettronico.feature_main.presentation.components.homework_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeworkByDateScreen(
    modifier: Modifier,
    groupedHomework: Map<LocalDate, List<Homework>>,
    onCheckedChange:(Int, Boolean) -> Unit
) {
    LazyColumn(modifier) {
        groupedHomework.forEach { (header, items) ->
            stickyHeader {
                DateItem(
                    date = header,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(
                items = items,
                key = { item ->
                    item.id
                }
            ) { i ->
                Spacer(modifier = Modifier.height(4.dp))
                HomeworkItem(homework = i, showDate = false) { id, state ->
                    onCheckedChange(id, state)
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}