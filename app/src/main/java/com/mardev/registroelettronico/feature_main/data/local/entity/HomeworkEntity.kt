package com.mardev.registroelettronico.feature_main.common.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.common.domain.model.Homework
import java.util.Date

@Entity
data class HomeworkEntity(
    val subject: String,
    val description: String,
    val dueDate: Date,
    val completed: Boolean,

    @PrimaryKey val id: Int
){
    fun toHomeWork(): Homework {
        return Homework(
            id = id,
            subject = subject,
            description = description,
            dueDate =dueDate,
            completed = completed
        )
    }
}
