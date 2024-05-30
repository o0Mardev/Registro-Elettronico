package com.mardev.registroelettronico.feature_main.presentation.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.core.presentation.AppState
import com.mardev.registroelettronico.core.presentation.navigation.Screen
import com.mardev.registroelettronico.core.presentation.navigation.screens
import com.mardev.registroelettronico.feature_main.presentation.components.absence.AbsenceScreen
import com.mardev.registroelettronico.feature_main.presentation.components.absence.AbsenceScreenViewModel
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

    // We need to add this because if the user uses pops out screen the selectedItemIndex remains unchanged
    navController.addOnDestinationChangedListener { _, currentDestination, _ ->
        selectedItemIndex = screens.indexOfFirst { it.route == currentDestination.route }

    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
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
        }) {
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
                    onDismiss = { /*TODO*/ },
                    onConfirm = { selectedOption ->
                        mainViewModel.onSaveStudentId(students[selectedOption])
                    }
                )
            }



            NavHost(
                navController,
                startDestination = Screen.Home.route,
                Modifier
                    .padding(innerPadding)
                    .padding(8.dp),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(250)
                    )
                },
                exitTransition = { ExitTransition.None },
            ) {
                composable(Screen.Home.route) {
                    val viewModel: HomeScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    HomeScreen(state, viewModel)
                }
                composable(Screen.Homework.route) {
                    val viewModel: HomeworkScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    HomeworkScreen(state) { id, checkBoxState ->
                        viewModel.checkHomework(id, checkBoxState)
                    }
                }
                composable(Screen.Lesson.route) {
                    val viewModel: LessonsScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    LessonScreen(state)
                }
                composable(Screen.Grade.route) {
                    val viewModel: GradeScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    GradeScreen(state)
                }
                composable(Screen.Absence.route) {
                    val viewModel: AbsenceScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    AbsenceScreen(state)
                }
                composable(Screen.Communication.route) {
                    val viewModel: CommunicationScreenViewModel = hiltViewModel()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    CommunicationScreen(state, viewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(userSettings)
                }
            }
        }
    }

}