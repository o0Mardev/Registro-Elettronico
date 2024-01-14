package com.mardev.registroelettronico.feature_settings.presentation

import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class UserSettingsImpl @Inject constructor(
    @Named("userPreferencesDatastore") private val dataStoreRepository: DataStoreRepository
) : UserSettings {
    override val themeStream: MutableStateFlow<AppTheme>
    override var theme: AppTheme by AppThemeDatastoreDelegate("app_theme", AppTheme.MODE_AUTO)

    init {
        themeStream = MutableStateFlow(theme)
    }

    inner class AppThemeDatastoreDelegate(
        private val name: String,
        private val default: AppTheme,
    ) : ReadWriteProperty<Any?, AppTheme> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): AppTheme =
            AppTheme.fromOrdinal(runBlocking {
                dataStoreRepository.getInt(name) ?: default.ordinal
            })

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppTheme) {
            themeStream.value = value
            runBlocking {
                dataStoreRepository.putInt(name, value.ordinal)
            }
        }
    }
}