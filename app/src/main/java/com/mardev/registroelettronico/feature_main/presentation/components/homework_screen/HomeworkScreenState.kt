package com.mardev.registroelettronico.feature_home.presentation.components.homework_screen

import com.mardev.registroelettronico.feature_home.domain.model.Homework

data class HomeworkScreenState(
    val homework: List<Homework> = emptyList(),
    val loading: Boolean = true
)
