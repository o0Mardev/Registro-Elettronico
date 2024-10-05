package com.mardev.registroelettronico.core.presentation.components

data class UpdateDialogState(
    val showUpdateDialog: Boolean = false,
    val showProgress: Boolean = false,
    val progress: Float = 0f,
    val showButtons: Boolean = true
)