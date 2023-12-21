package com.mardev.registroelettronico.feature_main.domain.model
data class DailyEvents(
    val homework: List<Homework> = emptyList(),
    val lessons: List<Lesson> = emptyList(),
    val grades: List<Grade> = emptyList(),
    val communications: List<Communication> = emptyList()
)
