package com.pratik.run.presentation.active_run

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import com.pratik.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
): BaseViewModel<ActiveRunAction,ActiveRunState>() {

    private val eventChannel = Channel<ActiveRunEvents>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)

    init {
        _hasLocationPermission
            .onEach { hasPermission ->
                if(hasPermission) {
                    runningTracker.startObservingLocation()
                } else{
                    runningTracker.startObservingLocation()
                }
            }
            .launchIn(viewModelScope)

        runningTracker.currentLocation
            .onEach { location->
                Timber.d("New Location:$location")
            }
            .launchIn(viewModelScope)
    }

    override fun initState() = ActiveRunState()

    override fun onAction(action: ActiveRunAction) {
        when(action) {
            ActiveRunAction.OnBackClick -> {}
            ActiveRunAction.OnFinishRunClick -> {}
            ActiveRunAction.OnResumeRunClick -> {}
            ActiveRunAction.OnToggleRunClick -> {}
            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                _hasLocationPermission.value = action.acceptedLocationPermission
                updateState { it.copy(showLocationRationale = action.showLocationRationale) }
            }
            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
                updateState { it.copy(showNotificationRationale = action.showNotificationRationale) }
            }

            ActiveRunAction.DismissRationaleDialog -> {
                updateState { it.copy(
                    showLocationRationale = false,
                    showNotificationRationale = false
                ) }
            }
        }
    }
}