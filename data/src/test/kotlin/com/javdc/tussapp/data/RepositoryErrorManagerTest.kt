package com.javdc.tussapp.data

import com.javdc.tussapp.data.repository.util.RepositoryErrorManager
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.ConnectException

class RepositoryErrorManagerTest {

    @Test
    fun `RepositoryErrorManager wrapper emits success when no exception is thrown`() {
        runBlocking {
            val asyncResult = RepositoryErrorManager.wrap {
                listOf<String>()
            }.last()

            assert(asyncResult is AsyncResult.Success)
        }
    }

    @Test
    fun `RepositoryErrorManager wrapper emits error when exception is thrown`() {
        runBlocking {
            val asyncResult = RepositoryErrorManager.wrap {
                throw Exception()
            }.last()

            assert(asyncResult is AsyncResult.Error)
            assert((asyncResult as? AsyncResult.Error)?.error is AsyncError.UnknownError)
        }
    }

    @Test
    fun `RepositoryErrorManager wrapper emits a loading at first`() {
        runBlocking {
            val asyncResult = RepositoryErrorManager.wrap {
                throw ConnectException()
            }.first()

            assert(asyncResult is AsyncResult.Loading)
        }
    }

}