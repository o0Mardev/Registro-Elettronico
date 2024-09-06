package com.mardev.registroelettronico.feature_main.data.remote.dto.structural

data class Response(
    val annoScolastico: String,
    val frazioniTemporali: List<FrazioniTemporali>,
    val infoCovid: InfoCovid,
    val motiviAssenza: List<MotiviAssenza>,
    val statiAvvisi: List<StatiAvvisi>,
    val tipoAssenze: List<TipoAssenze>,
    val tipoAutorizzazioni: List<TipoAutorizzazioni>,
    val tipoGiustificazione: List<TipoGiustificazione>,
    val tipoVoti: List<TipoVoti>
)