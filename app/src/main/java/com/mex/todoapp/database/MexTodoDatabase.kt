package com.mex.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mex.todoapp.database.dao.TaskDao
import com.mex.todoapp.database.model.TaskEntity


@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MexTodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}