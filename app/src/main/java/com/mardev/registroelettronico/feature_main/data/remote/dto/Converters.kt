package com.mardev.registroelettronico.feature_main.data.remote.dto

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Converters {
    @SuppressLint("SimpleDateFormat")
    fun stringToDate(dateString: String): Date {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.parse(dateString) ?: Date()
    }
}