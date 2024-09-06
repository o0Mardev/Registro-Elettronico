package com.mardev.registroelettronico.feature_main.presentation

import com.mardev.registroelettronico.feature_main.domain.model.Student
import com.mardev.registroelettronico.feature_main.domain.model.TimeFraction

data class MainScreenState(
    val showStudentSelector: Boolean = false,
    val showThreeDotsMenu: Boolean = false,
    val students: List<Student> = emptyList(),
    val timeFractions: List<TimeFraction> = emptyList()
)
