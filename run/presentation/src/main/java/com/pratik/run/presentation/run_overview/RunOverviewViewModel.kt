package com.pratik.run.presentation.run_overview

import com.pratik.core.presentation.designsystem.base.BaseViewModel

class RunOverviewViewModel constructor(

): BaseViewModel<RunOverviewAction,RunOverviewState>() {

    override fun initState() = RunOverviewState()

    override fun onAction(action: RunOverviewAction) {
    }
}