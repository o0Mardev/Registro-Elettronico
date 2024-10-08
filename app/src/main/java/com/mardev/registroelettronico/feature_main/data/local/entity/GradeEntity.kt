package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import java.time.LocalDate

@Entity
data class GradeEntity(
    val subject: String,
    val vote: String,
    val description: String,
    val date: LocalDate,
    val idTimeFraction: Int,
    val teacher: String,
    val weight: Float,
    val voteValue: Float,

    @ColumnInfo(defaultValue = "-1") val studentId: Int,
    @PrimaryKey val id: Int
) {
    fun toGrade(): Grade {
        return Grade(
            id,
            studentId,
            subject,
            vote,
            description,
            date,
            idTimeFraction,
            teacher,
            weight,
            voteValue
        )
    }
}
