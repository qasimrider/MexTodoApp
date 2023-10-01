package com.mex.todoapp.features.taskList

import com.mex.todoapp.data.model.Task
import com.mex.todoapp.model.FilterCriteria
import com.mex.todoapp.mvi.ViewAction

sealed class TaskListAction : ViewAction {
    object GetAllTasks : TaskListAction()
    class UpdateTask(val updatedTask: Task) : TaskListAction()
    class DeleteTask(val deletedTask: Task) : TaskListAction()
    class FilterTasks(val filterCriteria: FilterCriteria) : TaskListAction()
}