package com.mex.todoapp.mvi

import kotlinx.coroutines.flow.StateFlow


/**
 * This class should implemented by all the Viewmodels, sole purpose of this is to provide handles
 * to ViewModels to play with MVI(Model-View-Intent)
 */
interface IModel<STATE, INTENT> {

    /**
     * This holds the state of the viewModel
     */
    val uiState: StateFlow<UiState<STATE>>

    /**
     * This method is used by view to dispatch the initial intent of the view model
     */
    fun dispatchIntent(intent: INTENT)
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out R>(val data: R) : UiState<R>()
    object Initialization : UiState<Nothing>()
    //        data class Error(val errorResponse: ErrorResponse) : UiState<Nothing>()
}