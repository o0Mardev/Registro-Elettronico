package com.mardev.registroelettronico.feature_main.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.CommandJsonSerializer
import com.mardev.registroelettronico.core.data.remote.Data
import com.mardev.registroelettronico.core.data.remote.DataSerializer
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.data.remote.JsonRequestSerializer
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_main.data.local.dao.AbsenceDao
import com.mardev.registroelettronico.feature_main.data.local.dao.CommunicationDao
import com.mardev.registroelettronico.feature_main.data.local.dao.GradeDao
import com.mardev.registroelettronico.feature_main.data.local.dao.HomeworkDao
import com.mardev.registroelettronico.feature_main.data.local.dao.LessonDao
import com.mardev.registroelettronico.feature_main.data.local.dao.NoteDao
import com.mardev.registroelettronico.feature_main.data.local.dao.StudentDao
import com.mardev.registroelettronico.feature_main.data.local.dao.TimeFractionDao
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.domain.model.Note
import com.mardev.registroelettronico.feature_main.domain.model.Student
import com.mardev.registroelettronico.feature_main.domain.model.TimeFraction
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class RetrieveDataRepositoryImpl @Inject constructor(
    private val api: AxiosApi,
    private val homeworkDao: HomeworkDao,
    private val gradeDao: GradeDao,
    private val lessonDao: LessonDao,
    private val absenceDao: AbsenceDao,
    private val noteDao: NoteDao,
    private val communicationDao: CommunicationDao,
    private val studentDao: StudentDao,
    private val timeFractionDao: TimeFractionDao
) : RetrieveDataRepository {
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(JsonRequest::class.java, JsonRequestSerializer())
            .registerTypeAdapter(CommandJson::class.java, CommandJsonSerializer())
            .registerTypeAdapter(Data::class.java, DataSerializer()).create()

    override fun getAllHomework(
        request: JsonRequest
    ): Flow<Resource<List<Homework>>> = flow {
        val localHomework = homeworkDao.getHomework()
        emit(Resource.Loading(data = localHomework.map { it.toHomeWork() }))
        try {
            val homeworkResponseDto = api.getHomework(gson.toJson(request))
            val remoteHomeworks = homeworkResponseDto.response?.flatMap { homeworkDataDto ->
                homeworkDataDto.compiti.map { homeworkDto -> homeworkDto.copy(studentId = homeworkDataDto.idAlunno.toInt()) }
            }

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
                Timber.d("Deleted homework: $deletedHomework")
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newHomeworks = homeworkDao.getHomework()
        emit(Resource.Success(newHomeworks.map { it.toHomeWork() }))
    }


    override fun getAllLessons(
        request: JsonRequest
    ): Flow<Resource<List<Lesson>>> = flow {
        val localLessons = lessonDao.getLessons()
        emit(Resource.Loading(data = localLessons.map { it.toLesson() }))
        try {
            val lessonResponseDto = api.getLessons(gson.toJson(request))
            val remoteLessons = lessonResponseDto.response?.flatMap { lessonDataDto ->
                lessonDataDto.argomenti.map { lessonDto ->
                    lessonDto.copy(studentId = lessonDataDto.idAlunno.toInt())
                }
            }
            if (remoteLessons != null) {
                lessonDao.insertLessons(remoteLessons.map { it.toLessonEntity() })
                val deletedLessons = localLessons.filter { localItem ->
                    remoteLessons.none { remoteItem -> remoteItem.idArgomento == localItem.id }
                }
                Timber.d("Deleted lessons: $deletedLessons")
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newLessons = lessonDao.getLessons()
        emit(Resource.Success(newLessons.map { it.toLesson() }))
    }


    override fun getAllGrades(
        request: JsonRequest,
    ): Flow<Resource<List<Grade>>> = flow {
        val localGrades = gradeDao.getGrades()
        emit(Resource.Loading(data = localGrades.map { it.toGrade() }))
        try {
            val gradesResponseDto = api.getGrades(gson.toJson(request))
            val remoteGrades = gradesResponseDto.response?.flatMap { gradesDataDto ->
                gradesDataDto.voti.map { gradeDto ->
                    gradeDto.copy(
                        studentId = gradesDataDto.idAlunno.toInt(),
                        timeFractionId = gradesDataDto.idFrazione.toInt()
                    )
                }
            }

            if (remoteGrades != null) {
                gradeDao.insertGrades(remoteGrades.map { it.toGradeEntity() })

                val deletedGrades = localGrades.filter { localItem ->
                    remoteGrades.none { remoteItem -> remoteItem.idVoto == localItem.id }
                }
                Timber.d("Deleted grades: $deletedGrades")
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newGrades = gradeDao.getGrades()
        emit(Resource.Success(newGrades.map { it.toGrade() }))
    }

    override fun getAllAbsences(
        request: JsonRequest,
    ): Flow<Resource<List<GenericAbsence>>> = flow {
        val localAbsences = absenceDao.getAbsences()
        emit(Resource.Loading(data = localAbsences.map { it.toAbsence() }))
        try {

            val absenceResponseDto = api.getAbsences(gson.toJson(request))
            val remoteAbsences = absenceResponseDto.response?.flatMap { absencesDataDto ->
                absencesDataDto.assenze.map { absenceDto ->
                    absenceDto.copy(
                        studentId = absencesDataDto.idAlunno.toInt(),
                        timeFractionId = absencesDataDto.idFrazione.toInt()
                    )
                }
            }

            if (remoteAbsences!=null){
                absenceDao.insertAbsences(remoteAbsences.map { it.toAbsenceEntity() })

                val deletedAbsences = localAbsences.filter { localItem ->
                    remoteAbsences.none { remoteItem -> remoteItem.id == localItem.id }
                }
                Timber.d("Deleted absences: $deletedAbsences")
                absenceDao.deleteAbsencesByIds(deletedAbsences.map { it.id })
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newAbsences = absenceDao.getAbsences()
        emit(Resource.Success(newAbsences.map { it.toAbsence() }))
    }

    override fun getAllNotes(
        request: JsonRequest
    ): Flow<Resource<List<Note>>> = flow {
        val localNotes = noteDao.getNotes()
        emit(Resource.Loading(data = localNotes.map { it.toNote() }))
        try {

            val noteResponseDto = api.getNotes(gson.toJson(request))
            val remoteNotes = noteResponseDto.response?.flatMap { notesDataDto ->
                notesDataDto.note.map { noteDto ->
                    noteDto.copy(
                        studentId = notesDataDto.idAlunno.toInt(),
                        timeFractionId = notesDataDto.idFrazione.toInt()
                    )
                }
            }

            if (remoteNotes!=null){
                noteDao.insertNotes(remoteNotes.map { it.toNoteEntity() })

                val deletedAbsences = localNotes.filter { localItem ->
                    remoteNotes.none { remoteItem -> remoteItem.idNota == localItem.id }
                }
                Timber.d("Deleted notes: $deletedAbsences")
                noteDao.deleteNotesByIds(deletedAbsences.map { it.id })
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newNotes = noteDao.getNotes()
        emit(Resource.Success(newNotes.map { it.toNote() }))
    }

    override fun getAllCommunications(
        request: JsonRequest
    ): Flow<Resource<List<Communication>>> = flow {
        val cachedLocalCommunications = communicationDao.getCommunications()
        emit(Resource.Loading(cachedLocalCommunications.map { it.toCommunication() }))
        var isError = false
        try {
            val communicationResponseDto = api.getCommunications(gson.toJson(request))
            val remoteCommunications =
                communicationResponseDto.response?.flatMap { communicationDataDto ->
                    communicationDataDto.comunicazioni.map { communicationDto ->
                        communicationDto.copy(studentId = communicationDataDto.idAlunno.toInt())
                    }
                }

            if (remoteCommunications != null) {
                communicationDao.insertCommunications(remoteCommunications.map { it.toCommunicationEntity() })

                val localCommunications = communicationDao.getCommunications()

                val deletedCommunications = localCommunications.filter { localItem ->
                    remoteCommunications.none { remoteItem -> remoteItem.id == localItem.id }
                }
                Timber.d("Deleted communications: $deletedCommunications")
                communicationDao.deleteCommunicationsByIds(deletedCommunications.map { it.id })

                val newCommunications = localCommunications
                    .map { localCommunication ->
                        val remoteCommunication =
                            remoteCommunications.find { it.id == localCommunication.id }
                        val remoteAttachments =
                            remoteCommunication?.toAttachmentList() ?: emptyList()
                        localCommunication.toCommunication().copy(attachments = remoteAttachments)
                    }
                emit(Resource.Success(newCommunications))
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        } finally {
            if (isError) {
                val newCommunications = communicationDao.getCommunications()
                emit(Resource.Success(newCommunications.map { it.toCommunication() }))
            }
        }
    }

    override fun getAllStudents(request: JsonRequest): Flow<Resource<List<Student>>> = flow {
        val localStudents = studentDao.getStudents()
        emit(Resource.Loading(localStudents.map { it.toStudent() }))

        try {
            val remoteStudents = api.getStudents(
                gson.toJson(request)
            ).response


            if (remoteStudents != null) {
                studentDao.insertStudents(remoteStudents.map { it.toStudentEntity() })

                val deletedStudents = localStudents.filter { localItem ->
                    remoteStudents.none { remoteItem -> remoteItem.id == localItem.id }
                }

                studentDao.deleteStudentsByIds(deletedStudents.map { it.id })
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newStudents = studentDao.getStudents().map { it.toStudent() }
        emit(Resource.Success(newStudents))
    }

    override suspend fun updateHomeworkState(id: Int, state: Boolean) {
        homeworkDao.updateHomeworkState(id, state)
    }

    override suspend fun getHomeworkByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Homework>>> = flow {
        val dailyHomework = homeworkDao.getHomeworkByDate(date)
        if (studentId == null){
            emit(Resource.Success(dailyHomework.map { it.toHomeWork() }))
        } else {
            emit(Resource.Success(dailyHomework.filter { it.studentId == studentId }.map { it.toHomeWork() }))
        }
    }

    override suspend fun getLessonsByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Lesson>>> = flow {
        val dailyLessons = lessonDao.getLessonsByDate(date)
        if (studentId == null){
            emit(Resource.Success(dailyLessons.map { it.toLesson() }))
        } else {
            emit(Resource.Success(dailyLessons.filter { it.studentId == studentId }.map { it.toLesson() }))
        }
    }

    override suspend fun getGradesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Grade>>> = flow {
        val dailyGrades = gradeDao.getGradesByDate(date)
        if (studentId == null){
            emit(Resource.Success(dailyGrades.map { it.toGrade() }))
        } else {
            emit(Resource.Success(dailyGrades.filter { it.studentId == studentId }.map { it.toGrade() }))
        }
    }

    override suspend fun getAbsencesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<GenericAbsence>>> = flow {
        val dailyAbsences = absenceDao.getAbsencesByDate(date)
        if (studentId == null){
            emit(Resource.Success(dailyAbsences.map { it.toAbsence() }))
        }else {
            emit(Resource.Success(dailyAbsences.filter { it.studentId == studentId }.map { it.toAbsence() }))
        }
    }

    override suspend fun getCommunicationByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Communication>>> = flow {
        val dailyCommunication = communicationDao.getCommunicationsByDate(date)
        if (studentId == null){
            emit(Resource.Success(dailyCommunication.map { it.toCommunication() }))
        }else {
            emit(Resource.Success(dailyCommunication.filter { it.studentId == studentId }.map { it.toCommunication() }))
        }
    }

    override suspend fun getNotesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Note>>> = flow {
        val dailyNotes = noteDao.getNotesByDate(date)
        if (studentId == null) {
            emit(Resource.Success(dailyNotes.map { it.toNote() }))
        } else {
            emit(Resource.Success(dailyNotes.filter { it.studentId == studentId }.map { it.toNote() }))
        }

    }

    override fun getAllTimeFractions(
        request: JsonRequest
    ): Flow<Resource<List<TimeFraction>>> = flow {
        val localTimeFractions = timeFractionDao.getTimeFractions()
        emit(Resource.Loading(localTimeFractions.map { it.toTimeFraction() }))

        try {
            val remoteTimeFractions = api.getStructural(gson.toJson(request)).response?.frazioniTemporali?.flatMap { it.frazioni }

            if (remoteTimeFractions != null){
                timeFractionDao.insertTimeFractions(remoteTimeFractions.map { it.toTimeFractionEntity() })


                val deletedTimeFractions = localTimeFractions.filter { localItem ->
                    remoteTimeFractions.none { remoteItem -> remoteItem.idFrazione == localItem.id }
                }

                timeFractionDao.deleteTimeFractionsByIds(deletedTimeFractions.map { it.id })
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
                    uiText = UIText.StringResource(R.string.error2), data = null
                )
            )
        }
        val newTimeFractions = timeFractionDao.getTimeFractions().map { it.toTimeFraction() }
        emit(Resource.Success(newTimeFractions))
    }

    override suspend fun getTimeFractionById(id: Int): TimeFraction {
        return timeFractionDao.getTimeFractionById(id)
    }

}