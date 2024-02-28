package com.mardev.registroelettronico.feature_main.data.remote.dto.students

import com.mardev.registroelettronico.feature_main.data.local.entity.StudentEntity
import com.mardev.registroelettronico.feature_main.data.remote.dto.Converters

data class StudentDto(
    val avatar: String,
    val cognome: String,
    val dataNascita: String,
    val flagDocumenti: String,
    val flagGiustifica: String,
    val flagInvalsi: String,
    val flagPagoScuola: String,
    val id: Int,
    val idAlunno: String,
    val idPlesso: String,
    val nome: String,
    val security: String,
    val sesso: String,
    val userId: String
){
    fun toStudentEntity(): StudentEntity {
        return StudentEntity(
            name = nome,
            surname = cognome,
            birthDate = Converters.stringToDate(dataNascita),
            id = id,
            studentId = idAlunno.toInt()
        )
    }
}