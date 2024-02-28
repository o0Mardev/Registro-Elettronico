package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.LessonEntity
import java.time.LocalDate

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("SELECT * FROM lessonentity ORDER BY date Desc")
    suspend fun getLessons(): List<LessonEntity>

    @Query("DELETE FROM lessonentity")
    suspend fun deleteLessons()
    @Query("DELETE FROM lessonentity WHERE id IN (:ids)")
    suspend fun deleteLessonsByIds(ids: List<Int>)
    @Query("SELECT * FROM lessonentity WHERE date=:date")
    suspend fun getLessonsByDate(date: LocalDate): List<LessonEntity>
}