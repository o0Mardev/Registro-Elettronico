package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.presentation.components.absence_screen.AbsenceItem
import com.mardev.registroelettronico.feature_main.presentation.components.grade_screen.GradeItem
import com.mardev.registroelettronico.feature_main.presentation.components.homework_screen.HomeworkItem
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonItem
import com.mardev.registroelettronico.feature_main.presentation.components.note_screen.NoteItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    viewModel: HomeScreenViewModel,
    scrollBehaviorState: TopAppBarState
) {
    val noEventsAvailable = state.events.absences.isEmpty() && state.events.grades.isEmpty() && state.events.lessons.isEmpty() && state.events.homework.isEmpty()

    Column(
        modifier =  Modifier.then(
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .windowInsetsPadding(WindowInsets.displayCutout)
            } else Modifier,
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val collapsedFraction = scrollBehaviorState.collapsedFraction
        AnimatedVisibility(visible = collapsedFraction!=1f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.onSubtractDayButton()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                        contentDescription = null
                    )
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
                        val datePickerState =
                            rememberDatePickerState(initialSelectedDateMillis = (1000 * 60 * 60 * 24) * state.date.toEpochDay())
                        // When the state date changes we change also the datePickerState
                        LaunchedEffect(key1 = state.date) {
                            datePickerState.selectedDateMillis =
                                state.date.toEpochDay() * (1000 * 60 * 60 * 24)
                        }
                        var showDialog by rememberSaveable { mutableStateOf(false) }
                        if (showDialog) {
                            DatePickerDialog(
                                onDismissRequest = { showDialog = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showDialog = false
                                        datePickerState.selectedDateMillis?.let {
                                            viewModel.onSelectedDay(LocalDate.ofEpochDay(it / (1000 * 60 * 60 * 24)))
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
                                text = sdf1.format(state.date).toString()
                                    .replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Text(text = sdf2.format(state.date))
                        }
                    }
                }
                IconButton(onClick = {
                    viewModel.onAddDayButton()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = null
                    )
                }
            }
        }




        Column(
            Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (noEventsAvailable) {
                Text(
                    text = "Non ci sono eventi",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize()
                        .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(state.events.notes, key = { note -> note.id }) { note ->
                        NoteItem(
                            note = note,
                            showIcon = true,
                            showDate = false,
                            showOverline = true,
                            showDivider = true
                        )
                    }
                    items(state.events.absences, key = { absence -> absence.id }) { absence ->
                        AbsenceItem(
                            genericAbsence = absence,
                            showIcon = true,
                            showOverline = true,
                            showDivider = true
                        )
                    }

                    items(
                        state.events.homework,
                        key = { homework -> homework.id }) { homework ->
                        HomeworkItem(
                            homework = homework,
                            showDate = false,
                            showIcon = true,
                            showOverline = true,
                            showDivider = true
                        )
                    }

                    items(state.events.lessons, key = { lesson -> lesson.id }) { lesson ->
                        LessonItem(
                            lesson = lesson,
                            showDate = false,
                            showIcon = true,
                            showOverline = true,
                            showDivider = true
                        )
                    }
                    items(state.events.grades, key = { grade -> grade.id }) { grade ->
                        GradeItem(
                            grade = grade,
                            showDate = false,
                            showIcon = true,
                            showOverline = true,
                            showDivider = true
                        )
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