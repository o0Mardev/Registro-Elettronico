package com.mardev.registroelettronico.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.PlayLesson
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.PlayLesson
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.ui.graphics.vector.ImageVector
import com.mardev.registroelettronico.R

sealed class Screen(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val stringResourceId: Int,
) {
    object Homework : Screen(
        Icons.Filled.HomeWork,
        Icons.Outlined.HomeWork,
        "homework",
        R.string.homework
    )
    object Lesson : Screen(
        Icons.Filled.PlayLesson,
        Icons.Outlined.PlayLesson,
        "lesson",
        R.string.lesson
    )
    object Grade : Screen(
        Icons.Filled.RateReview,
        Icons.Outlined.RateReview,
        "grade",
        R.string.grade
    )
}
