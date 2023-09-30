package com.mex.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mex.todoapp.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertChat(chat: TaskEntity)

    @Query("SELECT * FROM Tasks ORDER BY title ASC")
    fun getTasks(): Flow<List<TaskEntity>>

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)
}