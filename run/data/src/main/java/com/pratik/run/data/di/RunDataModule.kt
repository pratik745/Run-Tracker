package com.pratik.run.data.di

import com.pratik.core.domain.run.SyncRunScheduler
import com.pratik.run.data.CreateRunWorker
import com.pratik.run.data.DeleteRunWorker
import com.pratik.run.data.FetchRunsWorker
import com.pratik.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}