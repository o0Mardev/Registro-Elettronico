package com.mardev.registroelettronico.feature_settings.di

import android.content.Context
import com.mardev.registroelettronico.core.data.repository.DataStoreRepositoryImpl
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    @Named("userPreferencesDatastore")
    fun provideUserPreferencesDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(app, Constants.userPreferencesDatastoreName)
    }

}