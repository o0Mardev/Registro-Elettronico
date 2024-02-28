package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.StudentEntity

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Query("SELECT * FROM studententity")
    suspend fun getStudents(): List<StudentEntity>

    @Query("DELETE FROM studententity WHERE id IN (:ids)")
    suspend fun deleteStudentsByIds(ids: List<Int>)
}