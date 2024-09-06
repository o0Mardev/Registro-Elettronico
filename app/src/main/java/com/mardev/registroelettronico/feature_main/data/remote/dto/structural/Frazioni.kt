package com.mardev.registroelettronico.feature_main.data.remote.dto.structural

import com.mardev.registroelettronico.feature_main.data.local.entity.TimeFractionEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class Frazioni(
    val dataFine: String,
    val dataInizio: String,
    val descFrazione: String,
    val idFrazione: Int
){
    fun toTimeFractionEntity(): TimeFractionEntity{
        return TimeFractionEntity(
            startDate = Converters.stringToDate(dataInizio),
            endDate = Converters.stringToDate(dataFine),
            description = descFrazione,
            id = idFrazione
        )
    }
}