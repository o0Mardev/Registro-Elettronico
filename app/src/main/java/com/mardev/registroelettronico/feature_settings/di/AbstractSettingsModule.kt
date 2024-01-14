package com.mardev.registroelettronico.feature_settings.di

import com.mardev.registroelettronico.feature_settings.presentation.UserSettings
import com.mardev.registroelettronico.feature_settings.presentation.UserSettingsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractSettingsModule {
    @Binds
    @Singleton
    abstract fun bindUserSettings(
        userSettingsImpl: UserSettingsImpl
    ): UserSettings
}