package com.mardev.registroelettronico.feature_main.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.core.presentation.AppState
import com.mardev.registroelettronico.core.presentation.navigation.Screen
import com.mardev.registroelettronico.core.presentation.navigation.screens
import com.mardev.registroelettronico.feature_main.presentation.components.about_screen.AboutScreen
import com.mardev.registroelettronico.feature_main.presentation.components.absence_screen.AbsenceScreen
import com.mardev.registroelettronico.feature_main.presentation.components.absence_screen.AbsenceScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.common.DropdownMenuWithRadioButtons
import com.mardev.registroelettronico.feature_main.presentation.components.communication_screen.CommunicationScreen
import com.mardev.registroelettronico.feature_main.presentation.components.communication_screen.CommunicationScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.grade_screen.GradeScreen
import com.mardev.registroelettronico.feature_main.presentation.components.grade_screen.GradeScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.home_screen.HomeScreen
import com.mardev.registroelettronico.feature_main.presentation.components.home_screen.HomeScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.homework_screen.HomeworkScreen
import com.mardev.registroelettronico.feature_main.presentation.components.homework_screen.HomeworkScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonScreen
import com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen.LessonsScreenViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.note_screen.NoteScreen
import com.mardev.registroelettronico.feature_main.presentation.components.note_screen.NoteScreenViewModel
import com.mardev.registroelettronico.feature_settings.presentation.UserSettings
import com.mardev.registroelettronico.feature_settings.presentation.components.AlertDialogWithRadioButtons
import com.mardev.registroelettronico.feature_settings.presentation.components.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    appState: AppState,
    userSettings: UserSettings,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    val periodOptions = listOf("Trimestre", "Pentamestre")

    var showThreeDotMenu by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("Pentamestre") } // Initial selected option

    //TODO These shouldn't be hardcoded but retrieved from database/api, also the description
    val timeFractionId: Int = when (selectedPeriod) {
        "Trimestre" -> {
            25227
        }

        "Pentamestre" -> {
            25228
        }

        else -> 0
    }


    // We need to add this because if the user uses pops out screen the selectedItemIndex remains unchanged
    navController.addOnDestinationChangedListener { _, currentDestination, _ ->
        selectedItemIndex = screens.indexOfFirst { it.route == currentDestination.route }
        showThreeDotMenu = false

    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                LazyColumn(modifier = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .windowInsetsPadding(WindowInsets.displayCutout)
                } else Modifier) {
                    item {
                        screens.forEachIndexed { index, screen ->
                            NavigationDrawerItem(
                                label = { Text(text = stringResource(id = screen.stringResourceId)) },
                                selected = selectedItemIndex == index,
                                icon = {
                                    Icon(
                                        imageVector = if (selectedItemIndex == index) screen.selectedIcon else screen.unselectedIcon,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    if (index != selectedItemIndex) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }
                                        selectedItemIndex = index
                                    }
                                })
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                    ),
                    scrollBehavior = scrollBehavior,
                    title = { Text(text = stringResource(id = screens[selectedItemIndex].stringResourceId)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        if (showThreeDotMenu) {
                            IconButton(onClick = { dropdownExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                            DropdownMenuWithRadioButtons(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                options = periodOptions,
                                selectedOption = selectedPeriod,
                                onOptionSelected = { option ->
                                    selectedPeriod = option

                                }
                            )
                        }
                    }
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(appState.snackbarHostState) }
        ) { innerPadding ->

            val students by mainViewModel.students.collectAsStateWithLifecycle()
            if (mainViewModel.showStudentSelector.value) {
                AlertDialogWithRadioButtons(
                    title = "Scegli lo studente",
                    options = students.map { "${it.name} ${it.surname}" },
                    indexSelectedOption = 0,
                    onDismiss = { },
                    onConfirm = { selectedOption ->
                        mainViewModel.onSaveStudentId(students[selectedOption])
                    }
                )
            }




            NavHost(
                navController,
                startDestination = Screen.Home.route,
                Modifier.padding(top = innerPadding.calculateTopPadding()),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(700)
                    )
                },
                exitTransition = {
                    fadeOut(tween(700))
                },
            ) {
                composable(Screen.Home.route) {
                    val viewModel: HomeScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    showThreeDotMenu = false
                    HomeScreen(state, viewModel, scrollBehavior.state)
                }
                composable(Screen.Homework.route) {
                    val viewModel: HomeworkScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    showThreeDotMenu = false
                    HomeworkScreen(state) { id, checkBoxState ->
                        viewModel.checkHomework(id, checkBoxState)
                    }
                }
                composable(Screen.Lesson.route) {
                    val viewModel: LessonsScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    showThreeDotMenu = false
                    LessonScreen(state)
                }
                composable(Screen.Grade.route) {
                    val viewModel: GradeScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    //TODO Try to call the update also when the screen is first launched.
                    viewModel.updateGradesForSelectedTimeFraction(timeFractionId)

//                    // Update the grades whenever the time fraction changes
//                    LaunchedEffect(timeFractionId) {
//                        viewModel.updateGradesForSelectedTimeFraction(timeFractionId)
//                    }

                    GradeScreen(state)
                    showThreeDotMenu = true
                }
                composable(Screen.Absence.route) {
                    val viewModel: AbsenceScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    //TODO Try to call the update also when the screen is first launched.
                    viewModel.updateAbsencesForSelectedTimeFraction(timeFractionId)


//                    LaunchedEffect(timeFractionId) {
//                        viewModel.updateAbsencesForSelectedTimeFraction(timeFractionId)
//                    }

                    AbsenceScreen(state)
                    showThreeDotMenu = true
                }
                composable(Screen.Notes.route) {
                    val viewModel: NoteScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    //TODO Try to call the update also when the screen is first launched.
                    viewModel.updateNotesForSelectedTimeFraction(timeFractionId)

//                    LaunchedEffect(timeFractionId) {
//                        viewModel.updateNotesForSelectedTimeFraction(timeFractionId)
//                    }

                    NoteScreen(state)
                    showThreeDotMenu = true

                }
                composable(Screen.Communication.route) {
                    val viewModel: CommunicationScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    showThreeDotMenu = false
                    CommunicationScreen(state, viewModel)
                }
                composable(Screen.Settings.route) {
                    showThreeDotMenu = false
                    SettingsScreen(userSettings)
                }
                composable(Screen.About.route) {
                    showThreeDotMenu = false
                    AboutScreen()
                }
            }
        }
    }
}
