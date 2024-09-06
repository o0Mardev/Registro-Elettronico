package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Homework(
    val id: Int,
    val subject: String,
    val description: String,
    val dueDate: LocalDate,
    val completed: Boolean,
    val studentId: Int
)
