package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Communication(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val read: Boolean,
    val attachments: List<Attachment>?
)
