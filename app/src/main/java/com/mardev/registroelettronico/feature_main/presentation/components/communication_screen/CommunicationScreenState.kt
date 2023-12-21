package com.mardev.registroelettronico.feature_home.presentation.components.communication_screen

import com.mardev.registroelettronico.feature_home.domain.model.Communication

data class CommunicationScreenState(
    val communications: List<Communication> = emptyList(),
    val loading: Boolean = true
)
