package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Note(
    val teacher: String,
    val idTimeFraction: Int,
    val studentId: Int,
    val description: String,
    val date: LocalDate,
    val type: Char,
    val id: Int
)
