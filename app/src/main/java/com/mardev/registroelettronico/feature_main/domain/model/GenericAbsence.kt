package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class GenericAbsence(
    val id: Int,
    val idTimeFraction: Int,
    val studentId: Int,
    val date: LocalDate,
    val typeOfAbsence: TypeOfAbsence,
    val dateJustification: LocalDate?,
    val typeJustification: String,
    val reasonOfJustification: String,
    val isCalculated: Boolean,
    val classTime: Int?,
    val time: String?,
)
enum class TypeOfAbsence {
    ABSENCE,
    DELAY,
    EXIT,
    UNKNOWN
}
