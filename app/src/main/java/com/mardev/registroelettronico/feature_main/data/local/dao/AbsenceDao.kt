package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.AbsenceEntity
import java.time.LocalDate

@Dao
interface AbsenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(absence: List<AbsenceEntity>)
    @Query("SELECT * FROM absenceentity ORDER BY date Desc")
    suspend fun getAbsences(): List<AbsenceEntity>
    @Query("SELECT * FROM absenceentity WHERE date=:date")
    suspend fun getAbsencesByDate(date: LocalDate): List<AbsenceEntity>
    @Query("DELETE FROM absenceentity WHERE id IN (:ids)")
    suspend fun deleteAbsencesByIds(ids: List<Int>)
}