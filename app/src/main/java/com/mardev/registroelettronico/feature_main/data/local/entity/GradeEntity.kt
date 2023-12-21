package com.mardev.registroelettronico.feature_main.common.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.common.domain.model.Grade
import java.util.Date

@Entity
data class GradeEntity(
    val subject: String,
    val vote: String,
    val description: String,
    val date: Date,
    val teacher: String,
    val weight: String,
    val voteValue: String,

    @PrimaryKey val id: Int
) {
    fun toGrade(): Grade {
        return Grade(
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
