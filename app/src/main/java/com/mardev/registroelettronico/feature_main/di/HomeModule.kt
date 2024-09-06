package com.mardev.registroelettronico.feature_main.di

import android.content.Context
import androidx.room.Room
import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.data.remote.Interceptor
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.data.local.Database
import com.mardev.registroelettronico.feature_main.data.repository.RetrieveDataRepositoryImpl
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import com.mardev.registroelettronico.feature_main.domain.use_case.GetAbsences
import com.mardev.registroelettronico.feature_main.domain.use_case.GetCommunications
import com.mardev.registroelettronico.feature_main.domain.use_case.GetEventsByDate
import com.mardev.registroelettronico.feature_main.domain.use_case.GetGrades
import com.mardev.registroelettronico.feature_main.domain.use_case.GetHomework
import com.mardev.registroelettronico.feature_main.domain.use_case.GetLessons
import com.mardev.registroelettronico.feature_main.domain.use_case.GetNotes
import com.mardev.registroelettronico.feature_main.domain.use_case.GetStudents
import com.mardev.registroelettronico.feature_main.domain.use_case.GetTimeFractions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideRetrieveDataApi(): AxiosApi {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(Interceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AxiosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): Database {
        return Room.databaseBuilder(
            app, Database::class.java, "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrieveDataRepository(
        api: AxiosApi,
        db: Database
    ): RetrieveDataRepository {
        return RetrieveDataRepositoryImpl(
            api,
            db.homeworkDao,
            db.gradeDao,
            db.lessonDao,
            db.absenceDao,
            db.noteDao,
            db.communicationDao,
            db.studentDao,
            db.timeFractionDao
        )
    }

    @Provides
    @Singleton
    fun provideGetHomeworkUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetHomework {
        return GetHomework(repository, sessionCache)
    }

    @Provides
    @Singleton
    fun provideGetLessonsUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetLessons {
        return GetLessons(repository, sessionCache)
    }

    @Provides
    @Singleton
    @Named("userPreferencesDatastore")
    fun provideGetGradesUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache,
        dataStoreRepository: DataStoreRepository
    ): GetGrades {
        return GetGrades(repository, sessionCache, dataStoreRepository)
    }

    @Provides
    @Singleton
    @Named("userPreferencesDatastore")
    fun provideGetAbsencesUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache,
        dataStoreRepository: DataStoreRepository
    ): GetAbsences {
        return GetAbsences(repository, sessionCache, dataStoreRepository)
    }

    @Provides
    @Singleton
    @Named("userPreferencesDatastore")
    fun provideGetNotesUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache,
        dataStoreRepository: DataStoreRepository
    ): GetNotes {
        return GetNotes(repository, sessionCache, dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideGetCommunicationsUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetCommunications {
        return GetCommunications(repository, sessionCache)
    }

    @Provides
    @Singleton
    fun provideGetStudentsUseCase(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetStudents{
        return GetStudents(repository, sessionCache)
    }

    @Provides
    @Singleton
    fun provideGetEventsByDate(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetEventsByDate {
        return GetEventsByDate(repository, sessionCache)
    }

    @Provides
    @Singleton
    fun provideGetTimeFractions(
        repository: RetrieveDataRepository,
        sessionCache: SessionCache
    ): GetTimeFractions {
        return GetTimeFractions(repository, sessionCache)
    }
}