package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Lesson(
    val id: Int,
    val studentId: Int,
    val subject: String,
    val description: String,
    val date: LocalDate,
    val timetablePeriod: String
)
