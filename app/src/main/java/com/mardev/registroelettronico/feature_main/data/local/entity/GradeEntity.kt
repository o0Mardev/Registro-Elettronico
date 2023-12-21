package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import java.util.Date

@Entity
data class GradeEntity(
    val subject: String,
    val vote: String,
    val description: String,
    val date: Date,
    val teacher: String,
    val weight: Float,
    val voteValue: Float,

    @PrimaryKey val id: Int
) {
    fun toGrade(): Grade {
        return Grade(
            id,
            subject,
            vote,
            description,
            date,
            teacher,
            weight,
            voteValue
        )
    }
}
