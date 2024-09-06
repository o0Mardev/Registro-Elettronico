package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import java.time.LocalDate

@Entity
data class LessonEntity(
    val subject: String,
    val description: String,
    val date: LocalDate,
    val timetablePeriod: String,

    @ColumnInfo(defaultValue = "-1") val studentId: Int,
    @PrimaryKey val id: Int
){
    fun toLesson(): Lesson{
        return Lesson(
            id,
            studentId,
            subject,
            description,
            date,
            timetablePeriod
        )
    }
}
