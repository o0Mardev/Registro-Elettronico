package com.mardev.registroelettronico.feature_main.presentation.components.communication_screen

import com.mardev.registroelettronico.feature_main.domain.model.Communication

data class CommunicationScreenState(
    val communications: List<Communication> = emptyList(),
    val loading: Boolean = true
)
