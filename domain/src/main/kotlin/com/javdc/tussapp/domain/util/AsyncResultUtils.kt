package com.javdc.tussapp.domain.util

import com.javdc.tussapp.common.util.areAllTheSame

inline fun <T, R> AsyncResult<T>.map(transform: (T) -> R): AsyncResult<R> {
    return when (this) {
        is AsyncResult.Loading -> AsyncResult.Loading()
        is AsyncResult.Success -> AsyncResult.Success(transform(this.data))
        is AsyncResult.Error -> AsyncResult.Error(this.error)
    }
}

fun List<AsyncResult<*>>.mapToSingleAsyncResult(): AsyncResult<Unit> {
    val errors = filterIsInstance<AsyncResult.Error<*>>().map { it.error }
    return if (errors.isNotEmpty()) {
        if (errors.areAllTheSame()) {
            AsyncResult.Error(errors.first())
        } else {
            AsyncResult.Error(AsyncError.UnknownError())
        }
    } else {
        AsyncResult.Success(Unit)
    }
}