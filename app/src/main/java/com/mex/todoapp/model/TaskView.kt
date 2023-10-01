package com.mex.todoapp.model

class TaskView(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val description: String = "",
    val category: Category = Category.WORK,
    val priority: Priority = Priority.LOW,
    val dueDate: Long = 0L,
    var isCompleted: Boolean = false
)