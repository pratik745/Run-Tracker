package com.pratik.run.presentation.run_overview

import com.pratik.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
