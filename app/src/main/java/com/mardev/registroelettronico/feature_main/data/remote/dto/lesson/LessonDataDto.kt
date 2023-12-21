package com.mardev.registroelettronico.feature_main.data.remote.dto.lesson

data class LessonDataDto(
    val argomenti: List<LessonDto>,
    val idAlunno: String
)