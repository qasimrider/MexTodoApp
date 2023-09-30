package com.mex.todoapp.data

import com.mex.todoapp.base.Result
import com.mex.todoapp.base.asResult
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.database.converters.toRoomEntity
import com.mex.todoapp.database.converters.toTask
import com.mex.todoapp.database.dao.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MexTodoRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : MexTodoRepository {
    override suspend fun insertTask(task: Task): Flow<Result<Unit>> {
        return flow { emit(taskDao.insertChat(task.toRoomEntity())) }.asResult()
    }

    override suspend fun updateTask(task: Task): Flow<Result<Unit>> {
        return flow { emit(taskDao.updateTask(task.toRoomEntity())) }.asResult()
    }

    override suspend fun deleteTask(task: Task): Flow<Result<Unit>> {
        return flow { emit(taskDao.deleteTask(task.toRoomEntity())) }.asResult()
    }

    override suspend fun getTasks(): Flow<Result<List<Task>>> {
        return taskDao.getTasks().map { it.map { it.toTask() } }.asResult()
    }
}