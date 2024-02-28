package com.mardev.registroelettronico.feature_main.data.remote.dto.communication

import com.mardev.registroelettronico.feature_main.data.local.entity.CommunicationEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters
import com.mardev.registroelettronico.feature_main.domain.model.Attachment

data class CommunicationDto(
    val allegati: List<AttachmentDto>?,
    val `data`: String,
    val desc: String,
    val flgObbl: String,
    val id: Int,
    val studentId: Int,
    val letta: String,
    val modificabile: String,
    val opzioni: String,
    val pin: String,
    val risposta: String,
    val risposta_testo: String,
    val tipo: String,
    val tipo_risposta: String,
    val titolo: String
){
    fun toCommunicationEntity():CommunicationEntity{
        return CommunicationEntity(
            id = id,
            studentId = studentId,
            title = titolo,
            description = desc,
            date = Converters.stringToDate(data),
            read = (letta=="S")
        )
    }

    fun toAttachmentList(): List<Attachment> {
        return allegati?.map { it.toAttachment() } ?: emptyList()
    }
}