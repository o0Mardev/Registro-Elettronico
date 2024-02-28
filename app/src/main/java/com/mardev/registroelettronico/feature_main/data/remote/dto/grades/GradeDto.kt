package com.mardev.registroelettronico.feature_main.data.remote.dto.grades

import com.mardev.registroelettronico.feature_main.data.local.entity.GradeEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class GradeDto(
    val commento: String,
    val `data`: String,
    val descMat: String,
    val docente: String,
    val idMat: String,
    val idVoto: Int,
    val peso: String,
    val tipo: String,
    val vistato: String,
    val vistatoData: String,
    val vistatoUtente: String,
    val voto: String,
    val votoValore: String,
    val timeFractionId: Int,
    val studentId: Int
) {
    fun toGradeEntity(): GradeEntity {
        return GradeEntity(
            subject = descMat,
            vote = voto,
            description = commento,
            id = idVoto,
            date = Converters.stringToDate(data),
            idTimeFraction = timeFractionId,
            teacher = docente,
            weight = peso.replace(",",".").toFloat(),
            voteValue = votoValore.replace(",",".").toFloat(),
            studentId = studentId
        )
    }
}