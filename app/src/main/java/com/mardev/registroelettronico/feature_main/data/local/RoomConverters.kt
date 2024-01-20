package com.mardev.registroelettronico.feature_main.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class RoomConverters {
    @TypeConverter
    fun longToLocalDate(longDate: Long): LocalDate {
        return LocalDate.ofEpochDay(longDate)
    }

    @TypeConverter
    fun localDateToLong(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }
}