package com.mex.todoapp.features.createTask

import androidx.compose.runtime.Immutable
import com.mex.todoapp.base.BaseViewModel
import com.mex.todoapp.base.Result
import com.mex.todoapp.base.UiState
import com.mex.todoapp.base.ViewState
import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.data.model.toTask
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.utility.launchViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val mexTodoRepository: MexTodoRepository) :
    BaseViewModel<CreateChatUiState>() {


    fun createTask(task: Task) {
        launchViewModelScope {
            mexTodoRepository.insertTask(task).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.emit(UiState.Success(CreateChatUiState()))
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }
}

@Immutable
class CreateChatUiState : ViewState