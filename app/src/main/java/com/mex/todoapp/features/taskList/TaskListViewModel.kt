package com.mex.todoapp.features.taskList

import androidx.compose.runtime.Immutable
import com.mex.todoapp.base.BaseViewModel
import com.mex.todoapp.base.Result
import com.mex.todoapp.base.UiState
import com.mex.todoapp.base.ViewState
import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.data.model.toView
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.utility.launchViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val mexTodoRepository: MexTodoRepository) :
    BaseViewModel<GetAllTasksUiState>() {

    private lateinit var getAllTasksUiState: GetAllTasksUiState

    fun getTasks() {
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

    fun updateTask(task: Task) {
        launchViewModelScope {
            mexTodoRepository.updateTask(task).collect { result ->
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

    fun deleteTask(task: Task) {
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
}

@Immutable
data class GetAllTasksUiState(
    val tasks: List<TaskView>,
    val isTaskUpdated: Boolean? = null,
    val isTaskDeleted: Boolean? = null
) : ViewState