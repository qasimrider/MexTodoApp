package com.mex.todoapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel<UIState : ViewState> : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<UIState>>(UiState.Initialization)
    val uiState = _uiState.asStateFlow()
}

interface ViewState
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out R>(val data: R) : UiState<R>()
    object Initialization : UiState<Nothing>()
    //        data class Error(val errorResponse: ErrorResponse) : UiState<Nothing>()
}