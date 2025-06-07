package com.pratik.core.data.di

import com.pratik.core.data.auth.EncryptedSessionStorageImpl
import com.pratik.core.data.network.HttpClientFactory
import com.pratik.core.data.run.OfflineFirstRunRepository
import com.pratik.core.domain.authSession.SessionStorage
import com.pratik.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorageImpl).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}