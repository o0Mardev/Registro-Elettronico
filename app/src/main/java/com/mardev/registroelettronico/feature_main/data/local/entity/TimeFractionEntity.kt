package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.TimeFraction
import java.time.LocalDate

@Entity
data class TimeFractionEntity(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val description: String,
    @PrimaryKey val id: Int
){
    fun toTimeFraction(): TimeFraction {
        return TimeFraction(
            startDate, endDate, description, id
        )
    }
}