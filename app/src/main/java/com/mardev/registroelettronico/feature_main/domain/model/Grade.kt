package com.mardev.registroelettronico.feature_main.domain.model

import java.util.Date

data class Grade(
    val id: Int,
    val subject: String,
    val vote: String,
    val description: String,
    val date: Date,
    val teacher: String,
    val weight: Float,
    val voteValue: Float
)
