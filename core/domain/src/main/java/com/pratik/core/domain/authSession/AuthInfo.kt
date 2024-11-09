package com.pratik.core.domain.authSession

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
