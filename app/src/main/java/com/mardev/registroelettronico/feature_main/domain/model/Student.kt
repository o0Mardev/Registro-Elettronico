package com.mardev.registroelettronico.feature_main.domain.model

import java.time.LocalDate

data class Student(
    val studentId: Int,
    val name: String,
    val surname: String,
    val birthDate: LocalDate
)
