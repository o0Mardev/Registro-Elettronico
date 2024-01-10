package com.mardev.registroelettronico.feature_authentication.di

import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.feature_authentication.data.repository.LoginRepositoryImpl
import com.mardev.registroelettronico.feature_authentication.data.repository.RememberMeImpl
import com.mardev.registroelettronico.feature_authentication.domain.repository.LoginRepository
import com.mardev.registroelettronico.feature_authentication.domain.repository.RememberMe
import com.mardev.registroelettronico.feature_authentication.domain.use_case.LoginUseCase
import com.mardev.registroelettronico.feature_authentication.domain.use_case.SearchSchoolUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: LoginRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRememberMe(@Named("userSessionDatastore") dataStoreRepository: DataStoreRepository): RememberMe{
        return RememberMeImpl(dataStoreRepository = dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        api: AxiosApi,
    ): LoginRepository {
        return LoginRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideSearchSchoolUseCase(
        api: AxiosApi
    ): SearchSchoolUseCase{
        return SearchSchoolUseCase(api)
    }

}