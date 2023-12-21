package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import com.mardev.registroelettronico.feature_main.domain.model.Lesson

data class LessonScreenState(
    val lessons: List<Lesson> = emptyList(),
    val loading: Boolean = true
)
