package com.pratik.auth.models.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpirationTimestamp: Long,
    val userId: String
)
