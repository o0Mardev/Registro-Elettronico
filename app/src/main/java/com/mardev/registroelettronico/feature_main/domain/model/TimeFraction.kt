package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class TimeFraction(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val description: String,
    val id: Int
)
