package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Grade(
    val id: Int,
    val studentId: Int,
    val subject: String,
    val vote: String,
    val description: String,
    val date: LocalDate,
    val idTimeFraction: Int,
    val teacher: String,
    val weight: Float,
    val voteValue: Float
)
