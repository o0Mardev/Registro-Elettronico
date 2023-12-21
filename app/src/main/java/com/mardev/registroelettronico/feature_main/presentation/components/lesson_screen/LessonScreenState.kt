package com.mardev.registroelettronico.feature_home.presentation.components.lesson_screen

import com.mardev.registroelettronico.feature_home.domain.model.Lesson

data class LessonScreenState(
    val lessons: List<Lesson> = emptyList(),
    val loading: Boolean = true
)
