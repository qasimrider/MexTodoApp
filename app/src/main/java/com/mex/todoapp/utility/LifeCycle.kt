package com.mex.todoapp.utility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ObserveUiState(block: suspend CoroutineScope.() -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            block.invoke(this)
        }
    }
}

fun ViewModel.launchViewModelScope(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch { CoroutineScope(Dispatchers.IO).launch { block() } }
}