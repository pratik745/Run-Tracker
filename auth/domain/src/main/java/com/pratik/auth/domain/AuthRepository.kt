package com.pratik.auth.domain

import com.pratik.core.domain.util.DataError
import com.pratik.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun login(email:String, password: String): EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String) : EmptyResult<DataError.Network>
}