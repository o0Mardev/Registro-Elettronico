package com.mardev.registroelettronico.feature_main.common.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.common.domain.model.Lesson
import java.util.Date

@Entity
data class LessonEntity(
    val subject: String,
    val description: String,
    val date: Date,
    val timetablePeriod: String,

    @PrimaryKey val id: Int
){
    fun toLesson(): Lesson {
        return Lesson(
            subject,
            description,
            date,
            timetablePeriod
        )
    }
}
