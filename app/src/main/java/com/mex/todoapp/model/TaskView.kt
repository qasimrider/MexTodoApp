package com.mex.todoapp.model

class TaskView(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Long,
    var isCompleted: Boolean = false
)