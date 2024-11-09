package com.pratik.core.data.di

import com.pratik.core.data.auth.EncryptedSessionStorageImpl
import com.pratik.core.data.network.HttpClientFactory
import com.pratik.core.domain.authSession.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorageImpl).bind<SessionStorage>()
}