package com.mardev.registroelettronico.feature_main.data.local

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.util.Date

class RoomConverters {
    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun longToDate(longDate: Long): Date {
        return Date(longDate)
    }

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }
}