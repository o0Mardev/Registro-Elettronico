package com.mardev.registroelettronico.feature_main.common.domain.model

import java.util.Date

data class Grade(
    val subject: String,
    val vote: String,
    val description: String,
    val date: Date,
    val teacher: String,
    val weight: String,
    val voteValue: String
)
