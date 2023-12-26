package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.domain.util.mapToSingleAsyncResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface SyncAllUseCase {
    suspend operator fun invoke(): AsyncResult<Unit>
}

class SyncAllUseCaseImpl(
    private val syncDateVersionsUseCase: SyncDateVersionsUseCase,
    private val syncStopsForTodayUseCase: SyncStopsForTodayUseCase,
    private val syncCardsUseCase: SyncCardsUseCase,
) : SyncAllUseCase {

    override suspend operator fun invoke(): AsyncResult<Unit> {
        return coroutineScope {
            return@coroutineScope listOf(
                async {
                    syncDateVersionsUseCase()
                    syncStopsForTodayUseCase()
                },
                async {
                    syncCardsUseCase()
                }
            ).awaitAll().mapToSingleAsyncResult()
        }
    }

}