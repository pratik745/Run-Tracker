package com.pratik.auth.data

import com.pratik.auth.domain.AuthRepository
import com.pratik.auth.models.request.LoginRequestModel
import com.pratik.auth.models.request.RegisterRequestModel
import com.pratik.auth.models.response.LoginResponseModel
import com.pratik.core.data.network.post
import com.pratik.core.domain.authSession.AuthInfo
import com.pratik.core.domain.authSession.SessionStorage
import com.pratik.core.domain.util.DataError
import com.pratik.core.domain.util.EmptyResult
import com.pratik.core.domain.util.Result
import com.pratik.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequestModel,LoginResponseModel>(
            route = LOGIN_ROUTE_ENDPOINT,
            body = LoginRequestModel(
                email = email,
                password = password
            )
        )
        if(result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequestModel, Unit>(
            route = REGISTER_ROUTE_ENDPOINT,
            body = RegisterRequestModel(
                email = email,
                password = password
            )
        )
    }

    companion object {
        const val REGISTER_ROUTE_ENDPOINT = "/register"
        const val LOGIN_ROUTE_ENDPOINT = "/login"
    }
}