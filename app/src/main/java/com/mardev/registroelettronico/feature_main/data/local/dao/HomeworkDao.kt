package com.mardev.registroelettronico.feature_main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mardev.registroelettronico.feature_main.data.local.entity.HomeworkEntity
import java.util.Date

@Dao
interface HomeworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(homeworks: List<HomeworkEntity>)
    @Query("SELECT * FROM homeworkentity ORDER BY dueDate Desc")
    suspend fun getHomework(): List<HomeworkEntity>

    @Query("SELECT * FROM homeworkentity WHERE dueDate=:date")
    suspend fun getHomeworkByDate(date: Date): List<HomeworkEntity>
    @Query("DELETE FROM homeworkentity")
    suspend fun deleteHomework()
    @Query("DELETE FROM HomeworkEntity WHERE id IN (:ids)")
    suspend fun deleteHomeworkByIds(ids: List<Int>)

    @Query("UPDATE homeworkentity SET completed = :state WHERE id=:id")
    suspend fun updateHomeworkState(id: Int, state: Boolean)

}