package com.mardev.registroelettronico.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mardev.registroelettronico.R

sealed class Screen(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val stringResourceId: Int,
) {
    data object Homework : Screen(
        Icons.Filled.Assignment,
        Icons.Outlined.Assignment,
        "homework",
        R.string.homework
    )

    data object Lesson : Screen(
        Icons.Filled.MenuBook,
        Icons.Outlined.MenuBook,
        "lesson",
        R.string.lesson
    )

    data object Grade : Screen(
        Icons.Filled.Grade,
        Icons.Outlined.Grade,
        "grade",
        R.string.grade
    )

    data object Communication : Screen(
        Icons.Filled.Newspaper,
        Icons.Outlined.Newspaper,
        "communication",
        R.string.communication
    )

    data object Home : Screen(
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "home",
        R.string.home
    )

    data object Settings: Screen(
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        "settings",
        R.string.settings
    )

}
val screens = listOf(
    Screen.Home,
    Screen.Homework,
    Screen.Lesson,
    Screen.Grade,
    Screen.Communication,
    Screen.Settings
)
