package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.StudentEntity
import com.mardev.registroelettronico.feature_main.data.local.entity.TimeFractionEntity
import com.mardev.registroelettronico.feature_main.domain.model.TimeFraction

@Dao
interface TimeFractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeFractions(timeFractions: List<TimeFractionEntity>)

    @Query("SELECT * FROM timefractionentity")
    suspend fun getTimeFractions(): List<TimeFractionEntity>

    @Query("SELECT * FROM timefractionentity WHERE id=:id")
    suspend fun getTimeFractionById(id: Int): TimeFraction

    @Query("DELETE FROM timefractionentity WHERE id IN (:ids)")
    suspend fun deleteTimeFractionsByIds(ids: List<Int>)
}