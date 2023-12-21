package com.mardev.registroelettronico.feature_main.common.domain.model

import java.util.Date

data class Communication(
    val title: String,
    val description: String,
    val date: Date,
    val read: Boolean,
    val attachments: List<Attachment>?
)
