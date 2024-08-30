package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.model.TypeOfAbsence
import java.time.LocalDate

@Entity
data class AbsenceEntity(
    val date: LocalDate,
    val dateJustification: LocalDate?,
    val typeOfAbsence: Char,
    val typeJustification: String,
    val reasonOfJustification: String,
    val isCalculated: Boolean,
    val studentId: Int,
    val classTime: Int?,
    val time: String?,
    @PrimaryKey val id: Int,
    val idTimeFraction: Int
) {
    fun toAbsence(): GenericAbsence {
        return GenericAbsence(
            id = id,
            date = date,
            idTimeFraction = idTimeFraction,
            typeOfAbsence = when (typeOfAbsence) {
                'A' -> TypeOfAbsence.ABSENCE
                'R' -> TypeOfAbsence.DELAY
                'U' -> TypeOfAbsence.EXIT
                else -> {TypeOfAbsence.UNKNOWN}
            },
            dateJustification = dateJustification,
            typeJustification = typeJustification,
            reasonOfJustification = reasonOfJustification,
            isCalculated = isCalculated,
            classTime = classTime,
            time = time,
        )
    }
}