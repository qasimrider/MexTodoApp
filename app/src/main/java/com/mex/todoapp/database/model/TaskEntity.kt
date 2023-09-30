package com.mex.todoapp.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mex.todoapp.model.Category
import com.mex.todoapp.model.Priority


@Entity(tableName = "Tasks")
data class TaskEntity(

    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Long,
    val isCompleted: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)