package com.mardev.registroelettronico.feature_main.domain.model

import java.util.Date

data class Homework(
    val id: Int,
    val subject: String,
    val description: String,
    val dueDate: Date,
    val completed: Boolean
)
