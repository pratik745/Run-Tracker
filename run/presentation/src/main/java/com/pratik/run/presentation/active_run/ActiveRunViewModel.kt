package com.pratik.run.presentation.active_run

import com.pratik.core.presentation.designsystem.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ActiveRunViewModel(): BaseViewModel<ActiveRunAction,ActiveRunState>() {

    private val eventChannel = Channel<ActiveRunEvents>()
    val events = eventChannel.receiveAsFlow()

    override fun initState() = ActiveRunState()

    override fun onAction(action: ActiveRunAction) {
        when(action) {
            ActiveRunAction.OnBackClick -> TODO()
            ActiveRunAction.OnFinishRunClick -> TODO()
            ActiveRunAction.OnResumeRunClick -> TODO()
            ActiveRunAction.OnToggleRunClick -> TODO()
        }
    }
}