package com.pratik.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnLogoutClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
}