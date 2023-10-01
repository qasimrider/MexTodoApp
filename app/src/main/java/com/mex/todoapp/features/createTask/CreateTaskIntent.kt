package com.mex.todoapp.features.createTask

import com.mex.todoapp.data.model.Task
import com.mex.todoapp.mvi.ViewIntent

sealed class CreateTaskIntent : ViewIntent {
    class CreateTask(val task: Task) : CreateTaskIntent()
}