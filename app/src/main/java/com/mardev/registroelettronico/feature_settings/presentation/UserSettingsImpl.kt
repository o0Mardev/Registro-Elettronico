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

    override val dynamicColorStream: MutableStateFlow<Boolean>
    override var dynamicColor: Boolean by DynamicColorDatastoreDelegate("app_dynamic_color", true)

    override val timeFractionIdStream: MutableStateFlow<Int>
    override var timeFractionId: Int by TimeFractionDatastoreDelegate("app_selected_time_fraction", -1)

    init {
        themeStream = MutableStateFlow(theme)
        dynamicColorStream = MutableStateFlow(dynamicColor)
        timeFractionIdStream = MutableStateFlow(timeFractionId)
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

    inner class DynamicColorDatastoreDelegate(
        private val name: String,
        private val default: Boolean
    ): ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
            return runBlocking { dataStoreRepository.getBoolean(name) ?: default }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            dynamicColorStream.value = value
            runBlocking { dataStoreRepository.putBoolean(name, value) }
        }
    }

    inner class TimeFractionDatastoreDelegate(
        private val name: String,
        private val default: Int
    ): ReadWriteProperty<Any?, Int>{
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
            return runBlocking { dataStoreRepository.getInt(name) ?: default}
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            timeFractionIdStream.value = value
            runBlocking { dataStoreRepository.putInt(name, value) }
        }

    }
}