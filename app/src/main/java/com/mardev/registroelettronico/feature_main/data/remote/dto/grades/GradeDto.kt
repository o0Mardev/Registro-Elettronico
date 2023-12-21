package com.mardev.registroelettronico.feature_main.common.data.remote.dto.grades

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
    val votoValore: String
) {
    fun toGradeEntity(): GradeEntity {
        return GradeEntity(
            subject = descMat,
            vote = voto,
            description = commento,
            id = idVoto,
            date = Converters.stringToDate(data),
            teacher = docente,
            weight = peso,
            voteValue = votoValore
        )
    }
}