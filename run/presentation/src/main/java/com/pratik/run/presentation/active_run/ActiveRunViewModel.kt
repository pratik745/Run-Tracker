package com.pratik.run.presentation.active_run

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import com.pratik.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
): BaseViewModel<ActiveRunAction,ActiveRunState>() {

    private val eventChannel = Channel<ActiveRunEvents>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.value.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily,state.value.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack,
        hasLocationPermission
    ) { shouldTrack, hasLocationPermission ->
        shouldTrack && hasLocationPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily,false)

    init {
        observeLocationPermission()
        observeTrackingState()
        observeRunningTracker()
    }

    override fun initState() = ActiveRunState()

    override fun onAction(action: ActiveRunAction) {
        when(action) {
            ActiveRunAction.OnBackClick -> {
                updateState { it.copy(shouldTrack = false) }
            }
            ActiveRunAction.OnFinishRunClick -> {}
            ActiveRunAction.OnResumeRunClick -> {
                updateState { it.copy(shouldTrack = true) }
            }
            ActiveRunAction.OnToggleRunClick -> {
                updateState { it.copy(
                    hasStartedRunning = true,
                    shouldTrack = !it.shouldTrack
                ) }
            }
            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
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

    private fun observeLocationPermission() {
        hasLocationPermission
            .onEach { hasPermission ->
                if(hasPermission) {
                    runningTracker.startObservingLocation()
                } else{
                    runningTracker.startObservingLocation()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeTrackingState() {
        isTracking
            .onEach { isTracking ->
                runningTracker.setIsTracking(isTracking)
            }
            .launchIn(viewModelScope)
    }

    private fun observeRunningTracker() {
        runningTracker
            .currentLocation
            .onEach { location->
                updateState { it.copy(currentLocation = location?.location) }
            }
            .launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach { runData ->
                updateState { it.copy(runData = runData) }
            }
            .launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach { elapsedTime ->
                updateState { it.copy(elapsedTime = elapsedTime) }
            }
            .launchIn(viewModelScope)
    }
}