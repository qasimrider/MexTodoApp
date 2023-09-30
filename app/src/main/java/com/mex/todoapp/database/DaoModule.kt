package com.mex.todoapp.database

import com.mex.todoapp.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesTaskDao(database: MexTodoDatabase): TaskDao = database.taskDao()

}