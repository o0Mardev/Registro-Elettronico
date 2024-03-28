package com.mardev.registroelettronico.feature_main.data.remote.dto.absences

import com.mardev.registroelettronico.feature_main.data.local.entity.AbsenceEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class AbsenceDto(
    val calcolo: Int,
    val `data`: String,
    val datagiust: String,
    val giustificabile: String,
    val id: Int,
    val motivo: String,
    val ora: String?,
    val oralez: String?,
    val tipo: Char,
    val tipogiust: String,
    val studentId: Int,
    val timeFractionId: Int,
){
    fun toAbsenceEntity(): AbsenceEntity{
        return AbsenceEntity(
            id = id,
            studentId = studentId,
            idTimeFraction = timeFractionId,
            date = Converters.stringToDate(data),
            dateJustification = if (datagiust.isNotBlank()) { Converters.stringToDate(datagiust)} else null,
            isCalculated = (calcolo!=0),
            reasonOfJustification = motivo,
            typeJustification = tipogiust,
            typeOfAbsence = tipo,
            time = ora,
            classTime = oralez?.toIntOrNull()
        )
    }
}