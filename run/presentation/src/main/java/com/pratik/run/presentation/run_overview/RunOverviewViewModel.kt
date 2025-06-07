package com.pratik.run.presentation.run_overview

import androidx.lifecycle.viewModelScope
import com.pratik.core.domain.authSession.SessionStorage
import com.pratik.core.domain.run.RunRepository
import com.pratik.core.domain.run.SyncRunScheduler
import com.pratik.core.presentation.designsystem.base.BaseViewModel
import com.pratik.run.presentation.run_overview.mapper.toRunUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class RunOverviewViewModel(
    private val runRepository: RunRepository,
    private val syncRunScheduler: SyncRunScheduler,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage
): BaseViewModel<RunOverviewAction,RunOverviewState>() {

    init {
        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                type = SyncRunScheduler.SyncType.FetchRuns(30.minutes)
            )
        }

        runRepository.getRuns().onEach { runs ->
            val runsUi = runs.map { it.toRunUi() }
            updateState { it.copy(runs = runsUi) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.syncPendingRuns()
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

    private fun logout() {
        applicationScope.launch {
            syncRunScheduler.cancelAllSyncs()
            runRepository.deleteAllRuns()
            runRepository.logout()
            sessionStorage.set(null)
        }
    }
}