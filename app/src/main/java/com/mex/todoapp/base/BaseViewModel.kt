package com.mex.todoapp.base

import androidx.lifecycle.ViewModel
import com.mex.todoapp.mvi.IModel
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.mvi.ViewAction
import com.mex.todoapp.mvi.ViewIntent
import com.mex.todoapp.mvi.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel<INTENT : ViewIntent, ACTION : ViewAction, STATE : ViewState> :
    ViewModel(), IModel<STATE, INTENT> {

    protected val _uiState = MutableStateFlow<UiState<STATE>>(UiState.Initialization)
    override val uiState: StateFlow<UiState<STATE>> = _uiState.asStateFlow()


    //region State Management
    final override fun dispatchIntent(intent: INTENT) {
        handleAction(reducer(intent))
    }

    protected abstract fun reducer(intent: INTENT): ACTION

    protected abstract fun handleAction(action: ACTION)
    //endregion
}