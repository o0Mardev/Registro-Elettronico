package com.mardev.registroelettronico.feature_home.presentation.components.grade_screen

import com.mardev.registroelettronico.feature_home.domain.model.Grade

data class GradeScreenState(
    val grades: List<Grade> = emptyList(),
    val loading: Boolean = true
)
