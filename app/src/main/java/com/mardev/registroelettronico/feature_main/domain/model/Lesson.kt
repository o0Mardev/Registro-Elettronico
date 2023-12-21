package com.mardev.registroelettronico.feature_main.common.domain.model

import java.util.Date

data class Lesson(
    val subject: String,
    val description: String,
    val date: Date,
    val timetablePeriod: String
)
