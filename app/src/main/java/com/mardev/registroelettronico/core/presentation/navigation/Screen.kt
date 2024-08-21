package com.mardev.registroelettronico.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.NoteAlt
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
        Icons.AutoMirrored.Filled.Assignment,
        Icons.AutoMirrored.Outlined.Assignment,
        "homework",
        R.string.homework
    )

    data object Lesson : Screen(
        Icons.AutoMirrored.Filled.LibraryBooks,
        Icons.AutoMirrored.Outlined.LibraryBooks,
        "lesson",
        R.string.lesson
    )

    data object Grade : Screen(
        Icons.Filled.Grade,
        Icons.Outlined.Grade,
        "grade",
        R.string.grade
    )

    data object Absence : Screen(
        Icons.Filled.EventBusy,
        Icons.Outlined.EventBusy,
        "absence",
        R.string.absence
    )

    data object Notes: Screen(
        Icons.Filled.NoteAlt,
        Icons.Outlined.NoteAlt,
        "note",
        R.string.note
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
    Screen.Absence,
    Screen.Notes,
    Screen.Communication,
    Screen.Settings
)
