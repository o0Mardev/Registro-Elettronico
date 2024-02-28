package com.mardev.registroelettronico.feature_main.data.remote.dto.students

data class StudentsResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<StudentDto>?
)