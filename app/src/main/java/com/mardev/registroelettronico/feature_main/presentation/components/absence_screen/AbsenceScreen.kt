package com.mardev.registroelettronico.feature_main.presentation.components.absence_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbsenceScreen(
    state: AbsenceScreenState,
) {
    val tabs = listOf("Assenze", "Ritardi", "Uscite")
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
        when (selectedTabIndex) {
            0 -> {
                val groupedAbsences = state.absences.groupBy { it.date }
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    groupedAbsences.forEach { (header, items) ->
                        stickyHeader {
                            DateItem(
                                date = header,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        items(items, key = { item -> item.id }) { absence ->
                            Spacer(modifier = Modifier.height(4.dp))
                            AbsenceItem(genericAbsence = absence)
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
            1 -> {
                val groupedDelays = state.delays.groupBy { it.date }
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    groupedDelays.forEach { (header, items) ->
                        stickyHeader {
                            DateItem(
                                date = header,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        items(items, key = { item -> item.id }) { absence ->
                            Spacer(modifier = Modifier.height(4.dp))
                            AbsenceItem(genericAbsence = absence)
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
            2 -> {
                val groupedExits = state.exits.groupBy { it.date }
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    groupedExits.forEach { (header, items) ->
                        stickyHeader {
                            DateItem(
                                date = header,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        items(items, key = { item -> item.id }) { absence ->
                            Spacer(modifier = Modifier.height(4.dp))
                            AbsenceItem(genericAbsence = absence)
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}
