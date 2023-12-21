package com.mardev.registroelettronico.feature_main.common.data.remote.dto.homework

data class HomeworkDataDto(
    val compiti: List<HomeworkDto>,
    val idAlunno: String
)