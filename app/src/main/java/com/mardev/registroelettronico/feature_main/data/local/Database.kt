package com.mardev.registroelettronico.feature_main.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mardev.registroelettronico.feature_main.data.local.dao.AbsenceDao
import com.mardev.registroelettronico.feature_main.data.local.dao.CommunicationDao
import com.mardev.registroelettronico.feature_main.data.local.dao.GradeDao
import com.mardev.registroelettronico.feature_main.data.local.dao.HomeworkDao
import com.mardev.registroelettronico.feature_main.data.local.dao.LessonDao
import com.mardev.registroelettronico.feature_main.data.local.dao.StudentDao
import com.mardev.registroelettronico.feature_main.data.local.entity.AbsenceEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.CommunicationEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.GradeEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.HomeworkEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.LessonEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.StudentEntity
import com.mardev.registroelettronico.feature_main.domain.model.Student

@Database(
    entities = [
        HomeworkEntity::class,
        LessonEntity::class,
        GradeEntity::class,
        AbsenceEntity::class,
        CommunicationEntity::class,
        StudentEntity::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 1, to = 3),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(RoomConverters::class)
abstract class Database : RoomDatabase() {
    abstract val homeworkDao: HomeworkDao
    abstract val lessonDao: LessonDao
    abstract val gradeDao: GradeDao
    abstract val absenceDao: AbsenceDao
    abstract val communicationDao: CommunicationDao
    abstract val studentDao: StudentDao
}