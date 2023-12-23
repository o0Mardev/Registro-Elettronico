package com.mardev.registroelettronico.feature_main.data.repository

import android.util.Log
import com.google.gson.Gson
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_main.data.local.dao.CommunicationDao
import com.mardev.registroelettronico.feature_main.data.local.dao.GradeDao
import com.mardev.registroelettronico.feature_main.data.local.dao.HomeworkDao
import com.mardev.registroelettronico.feature_main.data.local.dao.LessonDao
import com.mardev.registroelettronico.feature_main.data.remote.AxiosApi
import com.mardev.registroelettronico.feature_main.data.remote.CommandJson
import com.mardev.registroelettronico.feature_main.data.remote.JsonRequest
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class RetrieveDataRepositoryImpl @Inject constructor(
    private val api: AxiosApi,
    private val homeworkDao: HomeworkDao,
    private val gradeDao: GradeDao,
    private val lessonDao: LessonDao,
    private val communicationDao: CommunicationDao
) : RetrieveDataRepository {

    private val gson = Gson()

    override fun getAllHomework(
        request: JsonRequest
    ): Flow<Resource<List<Homework>>> = flow {
        val localHomework = homeworkDao.getHomework()
        emit(Resource.Loading(data = localHomework.map { it.toHomeWork() }))
        try {
            val remoteHomeworks = api.getHomework(
                gson.toJson(request)
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
                    uiText = UIText.StringResource(R.string.error1), data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.error2),
                    data = null
                )
            )
        }
        val newHomeworks = homeworkDao.getHomework().map { it.toHomeWork() }
        emit(Resource.Success(newHomeworks))
    }


    override fun getAllLessons(
        request: JsonRequest
    ): Flow<Resource<List<Lesson>>> =
        flow {
            val localLessons = lessonDao.getLessons()
            emit(Resource.Loading(data = localLessons.map { it.toLesson() }))
            try {
                val remoteLessons = api.getLessons(
                    gson.toJson(request)
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
                        uiText = UIText.StringResource(R.string.error1), data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        uiText = UIText.StringResource(R.string.error2),
                        data = null
                    )
                )
            }
            val newLessons = lessonDao.getLessons().map { it.toLesson() }
            emit(Resource.Success(newLessons))
        }


    override fun getAllGrades(
        request: JsonRequest
    ): Flow<Resource<List<Grade>>> =
        flow {
            val localGrades = gradeDao.getGrades()
            emit(Resource.Loading(data = localGrades.map { it.toGrade() }))
            try {
                val remoteGrades = api.getGrades(
                    gson.toJson(request)
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
                        uiText = UIText.StringResource(R.string.error1), data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        uiText = UIText.StringResource(R.string.error2),
                        data = null
                    )
                )
            }
            val newGrades = gradeDao.getGrades().map { it.toGrade() }
            emit(Resource.Success(newGrades))
        }

    override fun getAllCommunications(
        request: JsonRequest
    ): Flow<Resource<Pair<Int?, List<Communication>>>> = flow {
        val localCommunications = communicationDao.getCommunications()
        emit(Resource.Loading(Pair(null, localCommunications.map { it.toCommunication() })))
        var isError = false
        try {
            val remoteResponse = api.getCommunications(
                gson.toJson(request)
            ).response?.firstOrNull()

            if (remoteResponse != null) {
                val alunnoId = remoteResponse.idAlunno.toInt()
                val remoteCommunications = remoteResponse.comunicazioni
                communicationDao.insertCommunications(remoteCommunications.map { it.toCommunicationEntity() })

                val deletedCommunications = localCommunications.filter { localItem ->
                    remoteCommunications.none { remoteItem -> remoteItem.id == localItem.id }
                }
                Log.d("TAG", "getAllCommunications: deleted: $deletedCommunications")
                communicationDao.deleteCommunicationsByIds(deletedCommunications.map { it.id })

                val newCommunications = localCommunications.map { localCommunication ->
                    val remoteCommunication =
                        remoteCommunications.find { it.id == localCommunication.id }
                    val remoteAttachments = remoteCommunication?.toAttachmentList() ?: emptyList()
                    localCommunication.toCommunication().copy(attachments = remoteAttachments)
                }
                emit(Resource.Success(Pair(alunnoId, newCommunications)))
            }
        } catch (e: HttpException) {
            isError = true
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.error1), data = null
                )
            )
        } catch (e: IOException) {
            isError = true
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.error2),
                    data = null
                )
            )
        } finally {
            if (isError) {
                val newCommunications = communicationDao.getCommunications()
                emit(Resource.Success(Pair(null, newCommunications.map { it.toCommunication() })))
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
        val dailyCommunication =
            communicationDao.getCommunicationsByDate(date).map { it.toCommunication() }
        emit(Resource.Success(dailyCommunication))
    }

}