package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.GradeEntity
import java.time.LocalDate

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrades(grades: List<GradeEntity>)

    @Query("SELECT * FROM gradeentity ORDER BY date Desc")
    suspend fun getGrades(): List<GradeEntity>

    @Query("SELECT * FROM gradeentity WHERE date=:date")
    suspend fun getGradesByDate(date: LocalDate): List<GradeEntity>


    @Query("DELETE FROM gradeentity")
    suspend fun deleteGrades()

    @Query("DELETE FROM gradeentity WHERE id IN (:ids)")
    suspend fun deleteGradesByIds(ids: List<Int>)
}