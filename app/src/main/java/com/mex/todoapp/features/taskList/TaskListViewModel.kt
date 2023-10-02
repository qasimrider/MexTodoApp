package com.mex.todoapp.features.taskList

import androidx.compose.runtime.Immutable
import com.mex.todoapp.base.BaseViewModel
import com.mex.todoapp.base.Result
import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.data.model.toView
import com.mex.todoapp.model.Category
import com.mex.todoapp.model.FilterCriteria
import com.mex.todoapp.model.Priority
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.mvi.ViewState
import com.mex.todoapp.notification.NotificationScheduler
import com.mex.todoapp.utility.launchViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val mexTodoRepository: MexTodoRepository,
    private val notificationScheduler: NotificationScheduler,
) : BaseViewModel<TaskListIntent, TaskListAction, GetAllTasksUiState>() {

    private lateinit var getAllTasksUiState: GetAllTasksUiState

    override fun intentToAction(intent: TaskListIntent): TaskListAction {
        return when (intent) {
            is TaskListIntent.GetAllTasks -> TaskListAction.GetAllTasks
            is TaskListIntent.UpdateTask -> TaskListAction.UpdateTask(intent.updatedTask)
            is TaskListIntent.DeleteTask -> TaskListAction.DeleteTask(intent.deletedTask)
            is TaskListIntent.FilterTasks -> TaskListAction.FilterTasks(intent.filterCriteria)
        }
    }

    override fun reducer(action: TaskListAction) {
        when (action) {
            is TaskListAction.GetAllTasks -> getTasks()
            is TaskListAction.UpdateTask -> updateTask(action.updatedTask)
            is TaskListAction.DeleteTask -> deleteTask(action.deletedTask)
            is TaskListAction.FilterTasks -> {
                getAllTasksUiState = getAllTasksUiState.copy(filterCriteria = action.filterCriteria)
                checkForFilterCriteria()
            }
        }
    }

    //region Repository Calls
    private fun getTasks() {
        launchViewModelScope {
            mexTodoRepository.getTasks().collect { result ->
                when (result) {
                    is Result.Success -> {
                        getAllTasksUiState = if (::getAllTasksUiState.isInitialized) {
                            getAllTasksUiState.copy(
                                originalTasks = result.data,
                                filterCriteria = getAllTasksUiState.filterCriteria
                            )
                        } else {
                            GetAllTasksUiState(
                                tasks = result.data.map { it.toView() },
                                originalTasks = result.data
                            )
                        }
                        checkForFilterCriteria()
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }

    private fun updateTask(updatedTask: Task) {
        launchViewModelScope {
            mexTodoRepository.updateTask(updatedTask).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.emit(UiState.Success(getAllTasksUiState.copy(isTaskUpdated = true)))
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }

    private fun deleteTask(task: Task) {
        launchViewModelScope {
            mexTodoRepository.deleteTask(task).collect { result ->
                when (result) {
                    is Result.Success -> {
                        cancelNotification(task)
                        _uiState.emit(UiState.Success(getAllTasksUiState.copy(isTaskDeleted = true)))
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }
    //endregion

    private fun cancelNotification(task: Task) {
        notificationScheduler.cancelScheduledNotification(task.id.toInt())
    }

    private fun checkForFilterCriteria() {
        launchViewModelScope {
            getAllTasksUiState = getAllTasksUiState.copy(
                tasks = filterTasks(
                    getAllTasksUiState.originalTasks,
                    getAllTasksUiState.filterCriteria
                ).map { it.toView() },
            )
            _uiState.emit(UiState.Success(getAllTasksUiState))
        }

    }

    private fun filterTasks(tasks: List<Task>, filterCriteria: FilterCriteria): List<Task> {
        return tasks.filter { task ->
            with(filterCriteria) {
                val categoryMatch = category == Category.ALL || task.category == category
                val priorityMatch = priority == Priority.ALL || task.priority == priority
                val queryMatch = task.title.contains(query, ignoreCase = true)
                val isCompletedMatch = if (isCompleted) task.isCompleted else true

                categoryMatch && priorityMatch && queryMatch && isCompletedMatch
            }
        }
    }
}

@Immutable
data class GetAllTasksUiState(
    val tasks: List<TaskView>,
    val originalTasks: List<Task>,
    val isTaskUpdated: Boolean? = null,
    val isTaskDeleted: Boolean? = null,
    val filterCriteria: FilterCriteria = FilterCriteria()
) : ViewState