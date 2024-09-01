package com.pratik.core.domain.util


sealed interface Result<out D, out E:GenericError> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E:GenericError>(val error: E):Result<Nothing,E>
}

inline fun <T,E:GenericError,R> Result<T,E>.map(map:(T) -> R): Result<R,E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T,E: GenericError> Result<T,E>.asEmptyDataResult() : EmptyDataResult<E> = map {  }

typealias EmptyDataResult<E> = Result<Unit, E>