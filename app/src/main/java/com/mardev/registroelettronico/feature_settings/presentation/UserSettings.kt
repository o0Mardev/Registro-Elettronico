package com.mardev.registroelettronico.feature_settings.presentation
import com.mardev.registroelettronico.R
import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    MODE_AUTO,
    MODE_DAY,
    MODE_NIGHT;

    companion object {
        fun fromOrdinal(ordinal: Int) = entries[ordinal]
        fun resourceIdStringFromAppTheme(appTheme: AppTheme) = when(appTheme){
            MODE_AUTO -> R.string.follow_system
            MODE_DAY -> R.string.light_mode
            MODE_NIGHT -> R.string.dark_mode
        }
    }
}

interface UserSettings {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme

    val dynamicColorStream: StateFlow<Boolean>
    var dynamicColor: Boolean

    val timeFractionIdStream: StateFlow<Int>
    var timeFractionId: Int
}