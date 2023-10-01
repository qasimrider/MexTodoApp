package com.mex.todoapp.features.taskList

import androidx.compose.runtime.Immutable
import com.mex.todoapp.base.BaseViewModel
import com.mex.todoapp.base.Result
import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.data.model.toView
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.mvi.ViewState
import com.mex.todoapp.utility.launchViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val mexTodoRepository: MexTodoRepository) :
    BaseViewModel<TaskListIntent, TaskListAction, GetAllTasksUiState>() {

    private lateinit var getAllTasksUiState: GetAllTasksUiState

    override fun reducer(intent: TaskListIntent): TaskListAction {
        return when (intent) {
            is TaskListIntent.GetAllTasks -> TaskListAction.GetAllTasks
            is TaskListIntent.UpdateTask -> TaskListAction.UpdateTask(intent.updatedTask)
            is TaskListIntent.DeleteTask -> TaskListAction.DeleteTask(intent.deletedTask)
        }
    }

    override fun handleAction(action: TaskListAction) {
        when (action) {
            is TaskListAction.GetAllTasks -> getTasks()
            is TaskListAction.UpdateTask -> updateTask(action.updatedTask)
            is TaskListAction.DeleteTask -> deleteTask(action.deletedTask)
        }
    }

    //region Repository Calls
    private fun getTasks() {
        launchViewModelScope {
            mexTodoRepository.getTasks().collect { result ->
                when (result) {
                    is Result.Success -> {
                        getAllTasksUiState = GetAllTasksUiState(result.data.map { it.toView() })
                        _uiState.emit(UiState.Success(getAllTasksUiState))
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
                        _uiState.emit(UiState.Success(getAllTasksUiState.copy(isTaskDeleted = true)))
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }
    //endregion
}

@Immutable
data class GetAllTasksUiState(
    val tasks: List<TaskView>,
    val isTaskUpdated: Boolean? = null,
    val isTaskDeleted: Boolean? = null
) : ViewState