package com.mardev.registroelettronico.feature_main.data.remote.dto.notes

import com.mardev.registroelettronico.feature_main.data.local.entity.NoteEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class NoteDto(
    val `data`: String,
    val descDoc: String,
    val descMat: String?,
    val descNota: String,
    val idMat: String?,
    val idNota: Int,
    val isLetta: String,
    val tipo: Char,
    val vistatoData: String?,
    val vistatoUtente: String?,
    val studentId: Int,
    val timeFractionId: Int,
){
    fun toNoteEntity(): NoteEntity{
        return NoteEntity(
            id = idNota,
            teacher = descDoc,
            studentId = studentId,
            idTimeFraction = timeFractionId,
            date = Converters.stringToDate(data),
            description = descNota,
            type = tipo
        )
    }
}