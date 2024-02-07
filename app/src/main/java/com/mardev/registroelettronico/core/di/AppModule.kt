package com.mardev.registroelettronico.core.di

import android.content.Context
import com.mardev.registroelettronico.core.data.remote.AppUpdateApi
import com.mardev.registroelettronico.core.data.repository.AppUpdateRepositoryImpl
import com.mardev.registroelettronico.core.data.repository.DataStoreRepositoryImpl
import com.mardev.registroelettronico.core.domain.repository.AppUpdateRepository
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.domain.use_case.DownloadAppUpdateUseCase
import com.mardev.registroelettronico.core.domain.use_case.GetUpdateUrlUseCase
import com.mardev.registroelettronico.core.domain.use_case.InstallAppUpdateUseCase
import com.mardev.registroelettronico.core.domain.use_case.IsUpdateAvailableUseCase
import com.mardev.registroelettronico.core.domain.use_case.SaveUpdateUseCase
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Constants.BASE_URL_APP_UPDATE_API
import com.mardev.registroelettronico.feature_authentication.data.repository.SessionCacheImpl
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideSessionCache(@Named("userSessionDatastore") repository: DataStoreRepository): SessionCache {
        return SessionCacheImpl(repository)
    }


    @Singleton
    @Provides
    fun provideAppUpdateApi(): AppUpdateApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_APP_UPDATE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppUpdateApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppUpdateRepository(api: AppUpdateApi): AppUpdateRepository {
        return AppUpdateRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideIsUpdateAvailableUseCase(repository: AppUpdateRepository): IsUpdateAvailableUseCase{
        return IsUpdateAvailableUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideInstallAppUpdateUseCase(): InstallAppUpdateUseCase{
        return InstallAppUpdateUseCase()
    }

    @Singleton
    @Provides
    fun provideSaveUpdateUseCase(): SaveUpdateUseCase {
        return SaveUpdateUseCase()
    }


    @Singleton
    @Provides
    fun provideDownloadAppUpdateUseCase(repository: AppUpdateRepository): DownloadAppUpdateUseCase {
        return DownloadAppUpdateUseCase(repository)
    }


    @Singleton
    @Provides
    fun provideGetUpdateUrlUseCase(repository: AppUpdateRepository): GetUpdateUrlUseCase {
        return GetUpdateUrlUseCase(repository)
    }
}