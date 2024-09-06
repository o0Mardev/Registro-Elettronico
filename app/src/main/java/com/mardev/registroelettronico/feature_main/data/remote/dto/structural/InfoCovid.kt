package com.mardev.registroelettronico.feature_main.data.remote.dto.structural

data class InfoCovid(
    val data_accettazione: Any,
    val doc_enabled: String,
    val doc_file: String,
    val doc_required: String,
    val doc_text: String,
    val fam_enabled: String,
    val fam_file: String,
    val fam_pin: String,
    val fam_required: String,
    val fam_text: String
)