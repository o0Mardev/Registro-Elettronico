package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import java.time.LocalDate

@Entity
data class HomeworkEntity(
    val subject: String,
    val description: String,
    val dueDate: LocalDate,
    val completed: Boolean,

    @ColumnInfo(defaultValue = "-1") val studentId: Int,
    @PrimaryKey val id: Int
){
    fun toHomeWork(): Homework{
        return Homework(
            id = id,
            subject = subject,
            description = description,
            dueDate =dueDate,
            completed = completed,
            studentId = studentId
        )
    }
}
