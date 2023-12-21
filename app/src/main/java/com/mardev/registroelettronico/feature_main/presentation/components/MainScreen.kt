package com.mardev.registroelettronico.feature_home.presentation.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.core.presentation.AppState
import com.mardev.registroelettronico.core.presentation.navigation.Screen
import com.mardev.registroelettronico.feature_home.presentation.MainViewModel
import com.mardev.registroelettronico.feature_home.presentation.components.communication_screen.CommunicationScreen
import com.mardev.registroelettronico.feature_home.presentation.components.communication_screen.CommunicationScreenViewModel
import com.mardev.registroelettronico.feature_home.presentation.components.grade_screen.GradeScreen
import com.mardev.registroelettronico.feature_home.presentation.components.grade_screen.GradeScreenViewModel
import com.mardev.registroelettronico.feature_home.presentation.components.home_screen.HomeScreen
import com.mardev.registroelettronico.feature_home.presentation.components.home_screen.HomeScreenViewModel
import com.mardev.registroelettronico.feature_home.presentation.components.homework_screen.HomeworkScreen
import com.mardev.registroelettronico.feature_home.presentation.components.homework_screen.HomeworkScreenViewModel
import com.mardev.registroelettronico.feature_home.presentation.components.lesson_screen.LessonScreen
import com.mardev.registroelettronico.feature_home.presentation.components.lesson_screen.LessonsScreenViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    appState: AppState
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val state by mainViewModel.state

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    val screens = listOf(
        Screen.Home, Screen.Homework, Screen.Lesson, Screen.Grade, Screen.Communication
    )

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
                            if (selectedItemIndex != index) {
                                navController.navigate(screen.route)
                            }
                            selectedItemIndex = index
                        })
                }
            }
        }) {
        Scaffold(
            snackbarHost = { SnackbarHost(appState.snackbarHostState) }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                composable(Screen.Home.route) {
                    val viewModel: HomeScreenViewModel = hiltViewModel()
                    HomeScreen(viewModel.state.value)
                }
                composable(Screen.Homework.route) {
                    val viewModel: HomeworkScreenViewModel = hiltViewModel()
                    HomeworkScreen(viewModel.state.value) { id, state ->
                        viewModel.checkHomework(id, state)
                    }
                }
                composable(Screen.Lesson.route) {
                    val viewModel: LessonsScreenViewmodel = hiltViewModel()
                    LessonScreen(viewModel.state.value)
                }
                composable(Screen.Grade.route) {
                    val viewModel: GradeScreenViewModel = hiltViewModel()
                    GradeScreen(viewModel.state.value)
                }
                composable(Screen.Communication.route) {
                    val viewModel: CommunicationScreenViewModel = hiltViewModel()
                    CommunicationScreen(viewModel.state.value)
                }
            }
        }
    }

}