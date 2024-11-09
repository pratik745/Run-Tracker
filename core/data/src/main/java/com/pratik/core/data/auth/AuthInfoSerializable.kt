package com.pratik.core.data.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
