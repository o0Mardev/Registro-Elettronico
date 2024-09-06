package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Note
import java.time.LocalDate

@Entity
data class NoteEntity (
    val teacher: String,
    val idTimeFraction: Int,
    val description: String,
    val date: LocalDate,
    val type: Char,
    @ColumnInfo(defaultValue = "-1") val studentId: Int,
    @PrimaryKey val id: Int
){
    fun toNote(): Note {
        return Note(
            teacher = teacher,
            idTimeFraction = idTimeFraction,
            id = id,
            type = type,
            description = description,
            date = date,
            studentId = studentId
        )
    }
}