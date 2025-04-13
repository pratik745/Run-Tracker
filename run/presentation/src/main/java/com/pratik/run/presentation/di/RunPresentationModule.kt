package com.pratik.run.presentation.di

import com.pratik.run.domain.RunningTracker
import com.pratik.run.presentation.active_run.ActiveRunViewModel
import com.pratik.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}