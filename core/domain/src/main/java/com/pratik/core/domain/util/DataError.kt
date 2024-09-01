package com.pratik.core.domain.util

sealed interface DataError:GenericError {
    enum class Network: DataError {
        REQUEST_TIME_OUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUEST,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local:DataError {
        DISK_FULL
    }
}