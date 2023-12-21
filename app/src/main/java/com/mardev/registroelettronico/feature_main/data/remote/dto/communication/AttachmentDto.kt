package com.mardev.registroelettronico.feature_main.common.data.remote.dto.communication

import com.mardev.registroelettronico.feature_main.common.domain.model.Attachment

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