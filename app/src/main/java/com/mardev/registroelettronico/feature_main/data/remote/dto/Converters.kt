package com.mardev.registroelettronico.feature_main.data.remote.dto

import java.text.ParsePosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object Converters {
    fun stringToDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.from(formatter.parse(dateString, ParsePosition(0)))
    }
}