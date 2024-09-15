package com.pratik.core.presentation.designsystem.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<Action,State>: ViewModel() {
    protected abstract fun initState():State

    private val _state by lazy { MutableStateFlow(initState()) }
    val state = _state.asStateFlow()

    open fun onAction(action: Action) = Unit

    protected fun updateState(newState: (currentState:State) -> State) {
        _state.update { state->
            newState(state)
        }
    }
}