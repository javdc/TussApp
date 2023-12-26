package com.javdc.tussapp.data.repository.util

import co.touchlab.kermit.Logger
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.data.remote.model.GenericErrorDto
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

object RepositoryErrorManager {

    suspend inline fun <T> wrap(crossinline block: suspend () -> T): Flow<AsyncResult<T>> =
        flow {
            emit(AsyncResult.Loading())
            try {
                emit(AsyncResult.Success(block()))

            } catch (e: CancellationException) {
                // CancellationException should never be intercepted, throw it as-is
                throw e

            } catch (e: Exception) {
                emit(AsyncResult.Error(e.toAsyncError()))
            }
        }

    fun Exception.toAsyncError(): AsyncError {
        Logger.e("Caught exception in RepositoryErrorManager wrappers", this, "RepositoryErrorManager")
        return when (this) {
            is HttpException -> AsyncError.ServerError(
                code = code(),
                errorMessage = response()?.errorBody()?.getErrorMessage(),
            )
            is UnknownHostException -> AsyncError.ConnectionError
            is SocketTimeoutException -> AsyncError.TimeoutError
            is JsonParseException -> AsyncError.DataParseError
            else -> AsyncError.UnknownError(this)
        }
    }

    private fun ResponseBody.getErrorMessage(): String? =
        tryOrNull(logException = false) {
            Gson().fromJson(charStream(), GenericErrorDto::class.java).message
        }

}