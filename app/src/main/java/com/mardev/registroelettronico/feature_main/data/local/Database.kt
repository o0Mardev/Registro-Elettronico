package com.mardev.registroelettronico.feature_main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mardev.registroelettronico.feature_main.data.local.dao.CommunicationDao
import com.mardev.registroelettronico.feature_main.data.local.dao.GradeDao
import com.mardev.registroelettronico.feature_main.data.local.dao.HomeworkDao
import com.mardev.registroelettronico.feature_main.data.local.dao.LessonDao
import com.mardev.registroelettronico.feature_main.data.local.entity.CommunicationEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.GradeEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.HomeworkEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.LessonEntity

@Database(
    entities = [
        HomeworkEntity::class,
        LessonEntity::class,
        GradeEntity::class,
        CommunicationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class Database : RoomDatabase() {
    abstract val homeworkDao: HomeworkDao
    abstract val lessonDao: LessonDao
    abstract val gradeDao: GradeDao
    abstract val communicationDao: CommunicationDao
}