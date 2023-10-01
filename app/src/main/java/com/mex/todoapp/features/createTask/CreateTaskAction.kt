package com.mex.todoapp.features.createTask

import com.mex.todoapp.data.model.Task
import com.mex.todoapp.mvi.ViewAction

sealed class CreateTaskAction : ViewAction {
    class CreateTask(val task: Task) : CreateTaskAction()
}