package com.mex.todoapp.database.converters

import com.mex.todoapp.data.model.Task
import com.mex.todoapp.database.model.TaskEntity


fun Task.toRoomEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDate = dueDate,
    isCompleted = isCompleted
)

fun TaskEntity.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDate = dueDate,
    isCompleted = isCompleted

)