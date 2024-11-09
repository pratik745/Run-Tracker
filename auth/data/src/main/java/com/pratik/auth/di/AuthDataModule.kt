package com.pratik.auth.di

import com.pratik.auth.data.AuthRepositoryImpl
import com.pratik.auth.data.EmailPatternValidatorImpl
import com.pratik.auth.domain.AuthRepository
import com.pratik.auth.domain.EmailPatternValidator
import org.koin.core.module.dsl.singleOf
import com.pratik.auth.domain.UserDataValidator
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {

    single<EmailPatternValidator> {
        EmailPatternValidatorImpl()
    }

    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}