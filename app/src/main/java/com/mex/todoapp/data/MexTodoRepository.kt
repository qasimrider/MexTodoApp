package com.mex.todoapp.data

import com.mex.todoapp.base.Result
import com.mex.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow

interface MexTodoRepository {

    suspend fun insertTask(task: Task): Flow<Result<Unit>>
    suspend fun updateTask(task: Task): Flow<Result<Unit>>
    suspend fun deleteTask(task: Task): Flow<Result<Unit>>
    suspend fun getTasks(): Flow<Result<List<Task>>>
}