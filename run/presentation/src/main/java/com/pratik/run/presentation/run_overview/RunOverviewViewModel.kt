package com.pratik.run.presentation.run_overview

import androidx.lifecycle.viewModelScope
import com.pratik.core.domain.run.RunRepository
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import com.pratik.run.presentation.run_overview.mapper.toRunUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewModel(
    private val runRepository: RunRepository
): BaseViewModel<RunOverviewAction,RunOverviewState>() {

    init {
        runRepository.getRuns().onEach { runs ->
            val runsUi = runs.map { it.toRunUi() }
            updateState { it.copy(runs = runsUi) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.fetchRuns()
        }
    }

    override fun initState() = RunOverviewState()

    override fun onAction(action: RunOverviewAction) {
        when(action) {
            is RunOverviewAction.DeleteRun -> {
                viewModelScope.launch {
                    runRepository.deleteRun(action.runUi.id)
                }
            }
            RunOverviewAction.OnAnalyticsClick -> Unit
            RunOverviewAction.OnLogoutClick -> Unit
            RunOverviewAction.OnStartClick -> Unit
        }
    }
}