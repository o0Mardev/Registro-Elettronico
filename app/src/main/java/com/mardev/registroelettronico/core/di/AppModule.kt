package com.mardev.registroelettronico.core.di

import android.content.Context
import com.mardev.registroelettronico.core.data.repository.DataStoreRepositoryImpl
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.feature_authentication.data.repository.SessionCacheImpl
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @Named("userSessionDatastore")
    fun provideUserSessionDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(app, Constants.userSessionDatastoreName)
    }

    @Provides
    @Singleton
    @Named("userPreferencesDatastore")
    fun provideUserPreferencesDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(app, Constants.userPreferencesDatastoreName)
    }

    @Provides
    @Singleton
    fun provideSessionCache(@Named("userSessionDatastore") repository: DataStoreRepository): SessionCache {
        return SessionCacheImpl(repository)
    }
}