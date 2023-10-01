package com.mex.todoapp.features.taskList

import com.mex.todoapp.data.model.Task
import com.mex.todoapp.model.FilterCriteria
import com.mex.todoapp.mvi.ViewIntent

sealed class TaskListIntent : ViewIntent {
    object GetAllTasks : TaskListIntent()
    class UpdateTask(val updatedTask: Task) : TaskListIntent()
    class DeleteTask(val deletedTask: Task) : TaskListIntent()
    class FilterTasks(val filterCriteria: FilterCriteria) : TaskListIntent()
}