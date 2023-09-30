package com.mex.todoapp.di

import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.MexTodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun bindsTaskRepository(taskRepository: MexTodoRepositoryImpl): MexTodoRepository
}