package com.mardev.registroelettronico.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.core.presentation.components.UpdateDialog
import com.mardev.registroelettronico.feature_authentication.presentation.login_screen.components.LoginScreen
import com.mardev.registroelettronico.feature_authentication.presentation.search_screen.components.SearchScreen
import com.mardev.registroelettronico.feature_main.presentation.components.MainScreen
import com.mardev.registroelettronico.feature_main.presentation.components.MainViewModel
import com.mardev.registroelettronico.feature_main.presentation.components.home_screen.HomeScreenViewModel
import com.mardev.registroelettronico.feature_settings.presentation.AppTheme
import com.mardev.registroelettronico.feature_settings.presentation.UserSettings
import com.mardev.registroelettronico.ui.theme.RegistroElettronicoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSettings: UserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = userSettings.themeStream.collectAsState()
            val isDarkMode = when(theme.value){
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }

            val dynamicColor = userSettings.dynamicColorStream.collectAsState()

            val appState: AppState = rememberAppState()
            val navController = rememberNavController()

            RegistroElettronicoTheme(
                darkTheme = isDarkMode,
                dynamicColor = dynamicColor.value
            ) {
                UpdateDialog()
                NavHost(
                    navController = navController, startDestination = "authGraph",
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    navigation(
                        startDestination = "login",
                        route = "authGraph"
                    ) {
                        composable("login") { entry ->
                            val retrievedTaxCode = entry.savedStateHandle.get<String>("taxCode")
                            entry.savedStateHandle.remove<String>("taxCode")
                            LoginScreen(navController, appState, retrievedTaxCode)
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
                            MainScreen(appState, userSettings, viewModel)
                        }
                    }
                }
            }
        }
    }
}