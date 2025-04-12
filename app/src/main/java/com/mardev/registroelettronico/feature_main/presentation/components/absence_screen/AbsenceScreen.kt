package com.mardev.registroelettronico.feature_main.presentation.components.absence_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.model.TypeOfAbsence
import com.mardev.registroelettronico.feature_main.presentation.components.common.AlertDialogWithDropdown
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@Composable
fun AbsenceScreen(
    state: AbsenceScreenState,
    onJustifyAbsenceClick: (
        optionId: Int,
        absenceId: Int,
        typeOfAbsence: Char,
        pin: String,
        studentId: Int
    ) -> Unit
) {
    val tabs = listOf("Assenze", "Ritardi", "Uscite")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAbsence by remember { mutableStateOf<GenericAbsence?>(null) }

    Scaffold(
        topBar = {
            AbsenceTopBar(
                selectedTabIndex = selectedTabIndex,
                tabs = tabs,
                onTabSelected = { selectedTabIndex = it }
            )
        }
    ) { paddingValues ->
        when (selectedTabIndex) {
            0 -> AbsenceList(
                absences = state.absences,
                paddingValues = paddingValues,
                noItemsText = "Non sono presenti assenze per il seguente periodo.\nRicorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                onClick = { absence ->
                    selectedAbsence = absence
                    showDialog = true
                }
            )
            1 -> AbsenceList(
                absences = state.delays,
                paddingValues = paddingValues,
                noItemsText = "Non sono presenti ritardi per il seguente periodo.\nRicorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                onClick = { delay ->
                    selectedAbsence = delay
                    showDialog = true
                }
            )
            2 -> AbsenceList(
                absences = state.exits,
                paddingValues = paddingValues,
                noItemsText = "Non sono presenti uscite per il seguente periodo.\nRicorda di selezionare il periodo da visualizzare attraverso il menu in alto a destra.",
                onClick = { exit ->
                    selectedAbsence = exit
                    showDialog = true
                }
            )
        }

        if (showDialog && selectedAbsence != null) {
            JustificationDialog(
                state = state,
                onDismiss = { showDialog = false },
                onConfirm = { option, pin ->
                    showDialog = false
                    val optionId = state.typesOfJustification.find { it.first == option }?.second ?: -1
                    val typeOfAbsenceChar = when (selectedAbsence!!.typeOfAbsence) {
                        TypeOfAbsence.ABSENCE -> 'A'
                        TypeOfAbsence.DELAY -> 'R'
                        TypeOfAbsence.EXIT -> 'U'
                        TypeOfAbsence.UNKNOWN -> ' '
                    }
                    onJustifyAbsenceClick(
                        optionId,
                        selectedAbsence!!.id,
                        typeOfAbsenceChar,
                        pin,
                        selectedAbsence!!.studentId
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AbsenceList(
    absences: List<GenericAbsence>,
    paddingValues: PaddingValues,
    noItemsText: String,
    onClick: (GenericAbsence) -> Unit
) {
    val groupedAbsences = absences.groupBy { it.date }
    LazyColumn(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(), start = 2.dp, end = 2.dp
        )
    ) {
        if (groupedAbsences.isEmpty()) {
            item {
                Text(
                    text = noItemsText,
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
                items(items, key = { it.id }) { absence ->
                    AbsenceItem(genericAbsence = absence) { onClick(absence) }
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                }
            }
        }
    }
}

@Composable
private fun AbsenceTopBar(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(4.dp))
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) }
                ) {
                    Text(text = tabTitle)
                }
            }
        }
    }
}

@Composable
private fun JustificationDialog(
    state: AbsenceScreenState,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    AlertDialogWithDropdown(
        title = "Giustifica assenza",
        options = state.typesOfJustification.map { it.first },
        defaultOption = "Scegli un'opzione",
        onDismiss = onDismiss,
        onConfirmButton = onConfirm
    )
}

