package com.mardev.registroelettronico.feature_main.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.home.presentation.HomeScreenState
import com.mardev.registroelettronico.feature_main.home.presentation.HomeScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.grade_screen.GradeItem
import com.mardev.registroelettronico.feature_main.presentation.components.homework_screen.HomeworkItem
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonItem
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    viewModel: HomeScreenViewModel
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.onSubtractDayButton()
                }) {
                    Icon(imageVector = Icons.Default.NavigateBefore, contentDescription = null)
                }
                Card(
                    shape = CardDefaults.outlinedShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.65f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
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
                        Column {
                            val sdf1 = SimpleDateFormat("EEEE", Locale("it", "IT"))
                            val sdf2 = SimpleDateFormat("dd MMMM y", Locale("it", "IT"))
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
                    Icon(imageVector = Icons.Default.NavigateNext, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.events.grades.isEmpty() && state.events.lessons.isEmpty() && state.events.homework.isEmpty()) {
                    Text(
                        text = "Non ci sono eventi",
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                } else {
                    LazyColumn {
                        items(state.events.homework.size) { i ->
                            HomeworkItem(
                                //modifier = Modifier.background(Color.Blue),
                                homework = state.events.homework[i],
                                showDate = false,
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }
                        items(state.events.lessons.size) { i ->
                            LessonItem(
                                //modifier = Modifier.background(Color.Red),
                                lesson = state.events.lessons[i],
                                showDate = false,
                                showIcon = true,
                                showOverline = true,
                                showDivider = true
                            )
                        }
                        items(state.events.grades.size) { i ->
                            GradeItem(
                                //modifier = Modifier.background(Color.Magenta),
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