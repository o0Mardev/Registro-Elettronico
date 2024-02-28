package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Student
import java.time.LocalDate

@Entity
data class StudentEntity(
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    @PrimaryKey val id: Int,
    val studentId: Int
){
    fun toStudent(): Student{
        return Student(
            studentId = studentId,
            name = name,
            surname = surname,
            birthDate = birthDate
        )
    }
}