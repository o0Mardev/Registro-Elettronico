package com.mardev.registroelettronico.feature_authentication.di

import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.RC4
import com.mardev.registroelettronico.feature_authentication.data.remote.AuthenticationApi
import com.mardev.registroelettronico.feature_authentication.data.remote.Interceptor
import com.mardev.registroelettronico.feature_authentication.data.repository.LoginRepositoryImpl
import com.mardev.registroelettronico.feature_authentication.data.repository.RememberMeImpl
import com.mardev.registroelettronico.feature_authentication.domain.repository.LoginRepository
import com.mardev.registroelettronico.feature_authentication.domain.repository.RememberMe
import com.mardev.registroelettronico.feature_authentication.domain.use_case.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        api: AuthenticationApi,
    ): LoginRepository {
        return LoginRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideLoginApi(): AuthenticationApi {
        val rc4 = RC4("Cf4G8nepTqwyp2Y4suvQMdQJ".toByteArray())
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(Interceptor(rc4))
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenticationApi::class.java)
    }

}