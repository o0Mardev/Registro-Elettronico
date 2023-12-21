package com.mardev.registroelettronico.feature_main.domain.model

import java.util.Date

data class Lesson(
    val id: Int,
    val subject: String,
    val description: String,
    val date: Date,
    val timetablePeriod: String
)
