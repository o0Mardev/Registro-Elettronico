package com.mardev.registroelettronico.feature_main.data.remote.dto.communication

import com.mardev.registroelettronico.feature_main.domain.model.Attachment

data class AttachmentDto(
    val URL: String,
    val desc: String,
    val sourceName: String,
    val tipo: String
){
    fun toAttachment(): Attachment {
        return Attachment(
            type = tipo,
            name = sourceName,
            url = URL
        )
    }
}