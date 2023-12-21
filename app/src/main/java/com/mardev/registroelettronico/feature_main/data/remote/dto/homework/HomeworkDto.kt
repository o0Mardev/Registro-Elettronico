package com.mardev.registroelettronico.feature_main.common.data.remote.dto.homework

import com.mardev.registroelettronico.feature_main.data.local.entity.HomeworkEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class HomeworkDto(
    val `data`: String,
    val data_pubblicazione: String,
    val descCompiti: String,
    val descMat: String,
    val flagStato: String,
    val idCollabora: String,
    val idCompito: Int,
    val idMat: String,
    val tipo_nota: String
){
    fun toHomeworkEntity(completedState: Boolean): HomeworkEntity {
        return HomeworkEntity(
            id = idCompito,
            subject = descMat,
            description = descCompiti,
            dueDate = Converters.stringToDate(data),
            completed = completedState
        )
    }
}