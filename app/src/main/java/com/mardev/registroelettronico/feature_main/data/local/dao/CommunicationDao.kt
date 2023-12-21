package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mardev.registroelettronico.feature_main.data.local.entity.CommunicationEntity
import java.util.Date

@Dao
interface CommunicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunications(communications: List<CommunicationEntity>)

    @Transaction
    @Query("SELECT * FROM communicationentity ORDER BY date Desc")
    suspend fun getCommunications(): List<CommunicationEntity>

    @Query("DELETE FROM communicationentity")
    suspend fun deleteCommunications()

    @Query("DELETE FROM communicationentity WHERE id IN (:ids)")
    suspend fun deleteCommunicationsByIds(ids: List<Int>)
    @Query("SELECT * FROM communicationentity WHERE date=:date")
    suspend fun getCommunicationsByDate(date: Date): List<CommunicationEntity>
}