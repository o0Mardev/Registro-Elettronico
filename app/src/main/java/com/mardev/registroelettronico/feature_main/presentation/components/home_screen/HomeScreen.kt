package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.presentation.components.absence.AbsenceItem
import com.mardev.registroelettronico.feature_main.presentation.components.grade_screen.GradeItem
import com.mardev.registroelettronico.feature_main.presentation.components.homework_screen.HomeworkItem
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    viewModel: HomeScreenViewModel
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.onSubtractDayButton()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null)
                }
                Card(
                    modifier = Modifier.width(260.dp),
                    shape = CardDefaults.outlinedShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Spacer(modifier = Modifier.width(14.dp))
                        IconButton(modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                            onClick = {
                                viewModel.onCurrentDayButtonClick()
                            }
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onPrimary,
                                imageVector = Icons.Default.Today,
                                contentDescription = null
                            )
                        }
                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = (1000 * 60 *60 * 24) * state.date.toEpochDay())
                        // When the state date changes we change also the datePickerState
                        LaunchedEffect(key1 = state.date){
                            datePickerState.selectedDateMillis = state.date.toEpochDay() * (1000 * 60 *60 * 24)
                        }
                        var showDialog by rememberSaveable { mutableStateOf(false) }
                        if (showDialog) {
                            DatePickerDialog(
                                onDismissRequest = { showDialog = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showDialog = false
                                        datePickerState.selectedDateMillis?.let {
                                            viewModel.onSelectedDay(LocalDate.ofEpochDay(it/(1000 * 60 *60 * 24)))
                                        }
                                    }) {
                                        Text("Ok")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text("Annulla")
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState, showModeToggle = false)
                            }
                        }
                        Column(modifier = Modifier.clickable {
                            showDialog = true
                        }) {
                            val sdf1 = DateTimeFormatter.ofPattern("EEEE")
                            val sdf2 = DateTimeFormatter.ofPattern("dd MMMM y")
                            Text(
                                text = sdf1.format(state.date).toString().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Text(text = sdf2.format(state.date))
                        }
                    }
                }
                IconButton(onClick = {
                    viewModel.onAddDayButton()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.events.absences.isEmpty() && state.events.grades.isEmpty() && state.events.lessons.isEmpty() && state.events.homework.isEmpty()) {
                    Text(
                        text = "Non ci sono eventi",
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                } else {
                    LazyColumn {
                        items(state.events.absences.size){ i ->
                            AbsenceItem(
                                genericAbsence = state.events.absences[i],
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }

                        items(state.events.homework.size) { i ->
                            HomeworkItem(
                                homework = state.events.homework[i],
                                showDate = false,
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }
                        items(state.events.lessons.size) { i ->
                            LessonItem(
                                lesson = state.events.lessons[i],
                                showDate = false,
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }
                        items(state.events.grades.size) { i ->
                            GradeItem(
                                grade = state.events.grades[i],
                                showDate = false,
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }
                    }
                }
            }

        }
    }
}