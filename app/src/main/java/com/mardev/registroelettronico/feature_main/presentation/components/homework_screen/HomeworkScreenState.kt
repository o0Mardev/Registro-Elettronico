package com.mardev.registroelettronico.feature_main.presentation.components.homework_screen

import com.mardev.registroelettronico.feature_main.domain.model.Homework

data class HomeworkScreenState(
    val homework: List<Homework> = emptyList(),
    val loading: Boolean = true
)
