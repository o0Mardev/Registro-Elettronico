package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.NoteEntity
import java.time.LocalDate

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)
    @Query("SELECT * FROM noteentity ORDER BY date Desc")
    suspend fun getNotes(): List<NoteEntity>
    @Query("SELECT * FROM noteentity WHERE date=:date")
    suspend fun getNotesByDate(date: LocalDate): List<NoteEntity>
    @Query("DELETE FROM noteentity WHERE id IN (:ids)")
    suspend fun deleteNotesByIds(ids: List<Int>)
}