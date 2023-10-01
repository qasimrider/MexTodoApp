package com.mex.todoapp.features.createTask

import androidx.compose.runtime.Immutable
import com.mex.todoapp.base.BaseViewModel
import com.mex.todoapp.base.Result
import com.mex.todoapp.data.MexTodoRepository
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.mvi.ViewState
import com.mex.todoapp.notification.NotificationScheduler
import com.mex.todoapp.utility.launchViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val notificationScheduler: NotificationScheduler,
    private val mexTodoRepository: MexTodoRepository
) :
    BaseViewModel<CreateTaskIntent, CreateTaskAction, CreateChatUiState>() {

    override fun reducer(intent: CreateTaskIntent): CreateTaskAction =
        when (intent) {
            is CreateTaskIntent.CreateTask -> CreateTaskAction.CreateTask(intent.task)
        }

    override fun handleAction(action: CreateTaskAction) {
        when (action) {
            is CreateTaskAction.CreateTask -> createTask(action.task)
        }
    }

    //region Repository Calls
    private fun createTask(task: Task) {
        launchViewModelScope {
            mexTodoRepository.insertTask(task).collect { result ->
                when (result) {
                    is Result.Success -> {
                        createNotification(task)
                        _uiState.emit(UiState.Success(CreateChatUiState()))
                    }

                    is Result.Error -> TODO()
                    Result.Loading -> {}
                }
            }
        }
    }

    //endregion
    private fun createNotification(task: Task) {
        notificationScheduler.scheduleNotification(task)
    }
}

@Immutable
class CreateChatUiState : ViewState