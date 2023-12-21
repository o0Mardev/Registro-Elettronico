package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import java.util.Date

@Entity
data class LessonEntity(
    val subject: String,
    val description: String,
    val date: Date,
    val timetablePeriod: String,

    @PrimaryKey val id: Int
){
    fun toLesson(): Lesson{
        return Lesson(
            id,
            subject,
            description,
            date,
            timetablePeriod
        )
    }
}
