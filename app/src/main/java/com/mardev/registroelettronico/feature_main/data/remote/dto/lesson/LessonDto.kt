package com.mardev.registroelettronico.feature_main.data.remote.dto.lesson

import com.mardev.registroelettronico.feature_main.data.local.entity.LessonEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class LessonDto(
    val `data`: String,
    val data_pubblicazione: String,
    val descArgomenti: String,
    val descMat: String,
    val flagStato: String,
    val idArgomento: Int,
    val idCollabora: String,
    val idMat: String,
    val oreLezione: String,
    val studentId: Int
){
    fun toLessonEntity(): LessonEntity {
        return LessonEntity(
            subject = descMat,
            description = descArgomenti,
            date = Converters.stringToDate(data),
            id = idArgomento,
            studentId = studentId,
            timetablePeriod = oreLezione
        )
    }
}