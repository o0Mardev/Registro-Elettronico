package com.mardev.registroelettronico.feature_authentication.data.remote.dto.search

import com.mardev.registroelettronico.feature_authentication.domain.model.School

data class SchoolDataDto(
    val fsCF: String,
    val fsCap: String,
    val fsCitta: String,
    val fsIntitolazione: String,
    val fsNome: String,
    val fsProvincia: String,
    val fsRegione: String?
){
    fun toSchool(): School {
        return School(
            taxCode = fsCF,
            type = fsIntitolazione,
            name = fsNome,
            region = fsRegione ?: "",
            province = fsProvincia,
            city = fsCitta
        )
    }
}