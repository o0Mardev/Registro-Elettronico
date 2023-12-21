package com.mardev.registroelettronico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mardev.registroelettronico.core.presentation.AppState
import com.mardev.registroelettronico.core.presentation.rememberAppState
import com.mardev.registroelettronico.feature_authentication.presentation.components.LoginScreen
import com.mardev.registroelettronico.feature_home.presentation.components.HomeScreen
import com.mardev.registroelettronico.feature_home.presentation.components.grade_screen.GradeScreen
import com.mardev.registroelettronico.feature_home.presentation.components.homework_screen.HomeworkScreen
import com.mardev.registroelettronico.feature_home.presentation.components.lesson_screen.LessonScreen
import com.mardev.registroelettronico.ui.theme.RegistroElettronicoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroElettronicoTheme {
                val appState: AppState = rememberAppState()
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = "authGraph",
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    navigation(
                        startDestination = "login",
                        route = "authGraph"
                    ) {
                        composable("login") {
                            LoginScreen(navController, appState)
                        }
                    }
                    navigation(
                        startDestination = "home",
                        route = "mainGraph",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                            )
                        }
                    ) {
                        composable("home"){
                            HomeScreen(appState)
                        }
                    }
                }
            }
        }
    }
}