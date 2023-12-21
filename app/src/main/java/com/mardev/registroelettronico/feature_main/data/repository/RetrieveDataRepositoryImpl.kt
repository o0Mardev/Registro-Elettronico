package com.mardev.registroelettronico.feature_main.common.data.repository

import android.util.Log
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_main.data.local.dao.CommunicationDao
import com.mardev.registroelettronico.feature_main.data.local.dao.GradeDao
import com.mardev.registroelettronico.feature_main.data.local.dao.HomeworkDao
import com.mardev.registroelettronico.feature_main.data.local.dao.LessonDao
import com.mardev.registroelettronico.feature_main.data.remote.RetrieveDataApi
import com.mardev.registroelettronico.feature_main.common.domain.model.Communication
import com.mardev.registroelettronico.feature_main.common.domain.model.Grade
import com.mardev.registroelettronico.feature_main.common.domain.model.Homework
import com.mardev.registroelettronico.feature_main.common.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.common.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class RetrieveDataRepositoryImpl @Inject constructor(
    private val api: RetrieveDataApi,
    private val homeworkDao: HomeworkDao,
    private val gradeDao: GradeDao,
    private val lessonDao: LessonDao,
    private val communicationDao: CommunicationDao
) : RetrieveDataRepository {

    override fun getAllHomework(
        taxCode: String,
        userSession: String,
    ): Flow<Resource<List<Homework>>> = flow {
        val localHomework = homeworkDao.getHomework()
        emit(Resource.Loading(data = localHomework.map { it.toHomeWork() }))
        try {
            val remoteHomeworks = api.getHomework(
                taxCode, userSession
            ).response?.flatMap { homeworkDataDto -> homeworkDataDto.compiti }

            if (remoteHomeworks != null) {
                homeworkDao.insertHomework(remoteHomeworks.map {
                    it.toHomeworkEntity(
                        completedState = localHomework.firstOrNull { localItem ->
                            it.idCompito == localItem.id
                        }?.completed ?: false
                    )
                })
                val deletedHomework = localHomework.filter { localItem ->
                    remoteHomeworks.none { remoteItem -> remoteItem.idCompito == localItem.id }
                }
                Log.d("TAG", "getAllHomework: deleted: $deletedHomework")
                homeworkDao.deleteHomeworkByIds(deletedHomework.map { it.id })
            }

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    uiText = UIText.DynamicString("Oops something went wrong"), data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UIText.DynamicString("Couldn't reach server, check your internet connection"),
                    data = null
                )
            )
        }
        val newHomeworks = homeworkDao.getHomework().map { it.toHomeWork() }
        emit(Resource.Success(newHomeworks))
    }


    override fun getAllLessons(taxCode: String, userSession: String): Flow<Resource<List<Lesson>>> =
        flow {
            val localLessons = lessonDao.getLessons()
            emit(Resource.Loading(data = localLessons.map { it.toLesson() }))
            try {
                val remoteLessons = api.getLessons(
                    taxCode, userSession
                ).response?.flatMap { lessonDataDto -> lessonDataDto.argomenti }

                if (remoteLessons != null) {
                    lessonDao.insertLessons(remoteLessons.map { it.toLessonEntity() })
                    val deletedLessons = localLessons.filter { localItem ->
                        remoteLessons.none { remoteItem -> remoteItem.idArgomento == localItem.id }
                    }
                    Log.d("TAG", "getAllLessons: deleted: $deletedLessons")
                    lessonDao.deleteLessonsByIds(deletedLessons.map { it.id })
                }

            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        uiText = UIText.DynamicString("Oops something went wrong"), data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        uiText = UIText.DynamicString("Couldn't reach server, check your internet connection"),
                        data = null
                    )
                )
            }
            val newLessons = lessonDao.getLessons().map { it.toLesson() }
            emit(Resource.Success(newLessons))
        }


    override fun getAllGrades(taxCode: String, userSession: String): Flow<Resource<List<Grade>>> =
        flow {
            val localGrades = gradeDao.getGrades()
            emit(Resource.Loading(data = localGrades.map { it.toGrade() }))
            try {
                val remoteGrades = api.getGrades(
                    taxCode, userSession
                ).response?.flatMap { gradeDataDto -> gradeDataDto.voti }

                if (remoteGrades != null) {
                    gradeDao.insertGrades(remoteGrades.map { it.toGradeEntity() })

                    val deletedGrades = localGrades.filter { localItem ->
                        remoteGrades.none { remoteItem -> remoteItem.idVoto == localItem.id }
                    }
                    Log.d("TAG", "getAllGrades: deleted: $deletedGrades")
                    gradeDao.deleteGradesByIds(deletedGrades.map { it.id })
                }

            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        uiText = UIText.DynamicString("Oops something went wrong"), data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        uiText = UIText.DynamicString("Couldn't reach server, check your internet connection"),
                        data = null
                    )
                )
            }
            val newGrades = gradeDao.getGrades().map { it.toGrade() }
            emit(Resource.Success(newGrades))
        }

    override fun getAllCommunications(
        taxCode: String, userSession: String
    ): Flow<Resource<List<Communication>>> = flow {
        val localCommunications = communicationDao.getCommunications()
        emit(Resource.Loading(localCommunications.map { it.toCommunication() }))
        var isError = false
        try {

            val remoteCommunications = api.getCommunications(
                taxCode, userSession
            ).response?.flatMap { communicationDataDto -> communicationDataDto.comunicazioni }

            if (remoteCommunications != null) {
                communicationDao.insertCommunications(remoteCommunications.map { it.toCommunicationEntity() })

                val deletedCommunications = localCommunications.filter { localItem ->
                    remoteCommunications.none { remoteItem -> remoteItem.id == localItem.id }
                }
                Log.d("TAG", "getAllCommunications: deleted: $deletedCommunications")
                communicationDao.deleteCommunicationsByIds(deletedCommunications.map { it.id })

                val newCommunications = localCommunications.map { localCommunication ->
                    val remoteCommunication = remoteCommunications.find { it.id == localCommunication.id }
                    val remoteAttachments = remoteCommunication?.toAttachmentList() ?: emptyList()
                    localCommunication.toCommunication().copy(attachments = remoteAttachments)
                }
                emit(Resource.Success(newCommunications))
            }
        } catch (e: HttpException) {
            isError = true
            emit(
                Resource.Error(
                    uiText = UIText.DynamicString("Oops something went wrong"), data = null
                )
            )
        } catch (e: IOException) {
            isError = true
            emit(
                Resource.Error(
                    uiText = UIText.DynamicString("Couldn't reach server, check your internet connection"),
                    data = null
                )
            )
        } finally {
            if (isError){
                val newCommunicatios = communicationDao.getCommunications()
                emit(Resource.Success(newCommunicatios.map { it.toCommunication()}))
            }
        }
    }

    override suspend fun updateHomeworkState(id: Int, state: Boolean) {
        homeworkDao.updateHomeworkState(id, state)
    }

    override suspend fun getHomeworkByDate(
        date: Date
    ): Flow<Resource<List<Homework>>> = flow {
        Log.d("TAG", "getHomeworkByDate: for date = $date")
        val dailyHomework = homeworkDao.getHomeworkByDate(date).map { it.toHomeWork() }
        emit(Resource.Success(dailyHomework))
    }

    override suspend fun getLessonsByDate(
        date: Date
    ): Flow<Resource<List<Lesson>>> = flow {
        Log.d("TAG", "getLessonsByDate: for date = $date")
        val dailyLessons = lessonDao.getLessonsByDate(date).map { it.toLesson() }
        emit(Resource.Success(dailyLessons))
    }

    override suspend fun getGradesByDate(
        date: Date
    ): Flow<Resource<List<Grade>>> = flow {
        Log.d("TAG", "getGradesByDate: for date = $date")
        val dailyGrades = gradeDao.getGradesByDate(date).map { it.toGrade() }
        emit(Resource.Success(dailyGrades))
    }

    override suspend fun getCommunicationByDate(
        date: Date
    ): Flow<Resource<List<Communication>>> = flow {
        Log.d("TAG", "getCommunicationByDate: for date = $date")
        val dailyCommunication = communicationDao.getCommunicationsByDate(date).map { it.toCommunication() }
        emit(Resource.Success(dailyCommunication))
    }

}