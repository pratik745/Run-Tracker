package com.pratik.run.presentation.di

import com.pratik.run.presentation.active_run.ActiveRunViewModel
import com.pratik.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runOverviewViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}