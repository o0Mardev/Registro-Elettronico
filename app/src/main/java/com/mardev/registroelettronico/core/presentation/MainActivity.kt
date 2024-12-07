package com.mardev.registroelettronico.core.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.feature_authentication.presentation.login_screen.components.LoginScreen
import com.mardev.registroelettronico.feature_authentication.presentation.search_screen.components.SearchScreen
import com.mardev.registroelettronico.feature_main.presentation.components.MainScreen
import com.mardev.registroelettronico.feature_main.presentation.components.MainViewModel
import com.mardev.registroelettronico.feature_settings.presentation.AppTheme
import com.mardev.registroelettronico.feature_settings.presentation.UserSettings
import com.mardev.registroelettronico.ui.theme.RegistroElettronicoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSettings: UserSettings

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val theme = userSettings.themeStream.collectAsState()
            val isDarkMode = when (theme.value) {
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }

            val dynamicColor = userSettings.dynamicColorStream.collectAsState()

            val navController = rememberNavController()

            RegistroElettronicoTheme(
                darkTheme = isDarkMode,
                dynamicColor = dynamicColor.value
            ) {

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = event.message.asString(context),
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController, startDestination = "authGraph",
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        navigation(
                            startDestination = "login",
                            route = "authGraph"
                        ) {
                            composable("login") { entry ->
                                val retrievedTaxCode = entry.savedStateHandle.get<String>("taxCode")
                                entry.savedStateHandle.remove<String>("taxCode")
                                LoginScreen(navController, retrievedTaxCode)
                            }
                            composable("search") {
                                SearchScreen(navController)
                            }
                        }
                        navigation(
                            startDestination = "home",
                            route = "mainGraph",
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                    animationSpec = spring(
                                        Spring.DampingRatioLowBouncy,
                                        Spring.StiffnessLow
                                    )
                                )
                            }
                        ) {
                            composable("home") {
                                val viewModel: MainViewModel = hiltViewModel()
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                MainScreen(
                                    userSettings, state,
                                    onSaveStudentId = viewModel::onSaveStudentId,
                                    showThreeDotsMenu = viewModel::showThreeDotsMenu
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}