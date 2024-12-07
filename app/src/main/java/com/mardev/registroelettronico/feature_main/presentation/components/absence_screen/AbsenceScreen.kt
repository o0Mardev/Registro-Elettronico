package com.mardev.registroelettronico.feature_main.presentation.components.absence_screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
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
                LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), start = 2.dp, end = 2.dp).then(
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                    } else Modifier)) {
                    if (groupedAbsences.isEmpty()) {
                        item {
                            Text(
                                text = "Non sono presenti assenze per il seguente periodo.\n" +
                                        "Ricorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        groupedAbsences.forEach { (header, items) ->
                            stickyHeader {
                                DateItem(
                                    date = header,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            items(items, key = { item -> item.id }) { absence ->
                                AbsenceItem(genericAbsence = absence)
                                Spacer(modifier = Modifier.height(4.dp))
                                HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
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
            }

            1 -> {
                val groupedDelays = state.delays.groupBy { it.date }
                LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), start = 2.dp, end = 2.dp).then(
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.displayCutout)
                    } else Modifier)) {
                    if (groupedDelays.isEmpty()) {
                        item {
                            Text(
                                text = "Non sono presenti ritardi per il seguente periodo.\n" +
                                        "Ricorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        groupedDelays.forEach { (header, items) ->
                            stickyHeader {
                                DateItem(
                                    date = header,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            items(items, key = { item -> item.id }) { absence ->
                                AbsenceItem(genericAbsence = absence)
                                Spacer(modifier = Modifier.height(4.dp))
                                HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
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
            }

            2 -> {
                val groupedExits = state.exits.groupBy { it.date }
                LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), start = 2.dp, end = 2.dp).then(
                        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            Modifier
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .windowInsetsPadding(WindowInsets.displayCutout)
                        } else Modifier)) {
                    if (groupedExits.isEmpty()) {
                        item {
                            Text(
                                text = "Non sono presenti uscite per il seguente periodo.\n" +
                                        "Ricorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        groupedExits.forEach { (header, items) ->
                            stickyHeader {
                                DateItem(
                                    date = header,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            items(items, key = { item -> item.id }) { absence ->
                                AbsenceItem(genericAbsence = absence)
                                Spacer(modifier = Modifier.height(4.dp))
                                HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
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
            }
        }
    }
}
