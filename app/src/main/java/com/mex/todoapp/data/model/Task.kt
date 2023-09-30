package com.mex.todoapp.data.model

import com.mex.todoapp.model.Category
import com.mex.todoapp.model.Priority
import com.mex.todoapp.model.TaskView

class Task(
    val id: Long,
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Long,
    val isCompleted: Boolean
)

fun Task.toView() = TaskView(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDate = dueDate,
    isCompleted = isCompleted
)

fun TaskView.toTask() = Task(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDate = dueDate,
    isCompleted = isCompleted
)

