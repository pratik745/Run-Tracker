package com.pratik.auth.models.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestModel(
    val email: String,
    val password: String
)
