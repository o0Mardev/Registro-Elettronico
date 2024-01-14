package com.mardev.registroelettronico.feature_settings.presentation
import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    MODE_AUTO,
    MODE_DAY,
    MODE_NIGHT;

    companion object {
        fun fromOrdinal(ordinal: Int) = entries[ordinal]
    }
}

interface UserSettings {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme
}