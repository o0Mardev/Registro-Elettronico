package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import com.mardev.registroelettronico.feature_main.domain.model.Grade

data class GradeScreenState(
    val allGrades: List<Grade> = emptyList(),
    val filteredGrades: List<Grade> = emptyList(),
    val loading: Boolean = true
)
