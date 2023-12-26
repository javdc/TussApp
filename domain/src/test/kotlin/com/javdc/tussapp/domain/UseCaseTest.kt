package com.javdc.tussapp.domain

import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.usecase.GetCardStatusUseCase
import com.javdc.tussapp.domain.usecase.GetCardStatusUseCaseImpl
import com.javdc.tussapp.domain.usecase.SyncStopsForTodayUseCase
import com.javdc.tussapp.domain.usecase.SyncStopsForTodayUseCaseImpl
import com.javdc.tussapp.domain.util.AsyncResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UseCaseTest {

    @Test
    fun `SyncStopsForTodayUseCase returns a success when version is already synced`() = runBlocking {
        /* Given */
        val stopRepository = mockk<StopRepository> {
            coEvery { getStopsSyncedVersion() } returns 1
        }

        val dateRepository = mockk<DateRepository> {
            coEvery { getVersion(any()) } returns 1
        }

        val syncStopsForTodayUseCase: SyncStopsForTodayUseCase = SyncStopsForTodayUseCaseImpl(stopRepository, dateRepository)

        /* When */
        val result = syncStopsForTodayUseCase()

        /* Then */
        assert(result is AsyncResult.Success)
    }

    @Test
    fun `SyncStopsForTodayUseCase returns a success when syncStopsInLocal returns a success`() = runBlocking {
        /* Given */
        val stopRepository = mockk<StopRepository> {
            coEvery { getStopsSyncedVersion() } returns 1
            coEvery { syncStopsInLocal(any()) } returns AsyncResult.Success(Unit)
        }

        val dateRepository = mockk<DateRepository> {
            coEvery { getVersion(any()) } returns 2
        }

        val syncStopsForTodayUseCase: SyncStopsForTodayUseCase = SyncStopsForTodayUseCaseImpl(stopRepository, dateRepository)

        /* When */
        val result = syncStopsForTodayUseCase()

        /* Then */
        assert(result is AsyncResult.Success)
    }

    @Test
    fun `SyncStopsForTodayUseCase calls syncStopsInLocal when version is not synced`() = runBlocking {
        /* Given */
        val stopRepository = mockk<StopRepository> {
            coEvery { getStopsSyncedVersion() } returns 1
            coEvery { syncStopsInLocal(any()) } returns AsyncResult.Success(Unit)
        }

        val dateRepository = mockk<DateRepository> {
            coEvery { getVersion(any()) } returns 2
        }

        val syncStopsForTodayUseCase: SyncStopsForTodayUseCase = SyncStopsForTodayUseCaseImpl(stopRepository, dateRepository)

        /* When */
        syncStopsForTodayUseCase()

        /* Then */
        coVerify { stopRepository.syncStopsInLocal(any()) }
    }

    @Test
    fun `GetCardStatusUseCase returns a success when the repository returns a success`() = runBlocking {
        /* Given */
        val repository = mockk<CardRepository> {
            coEvery { getCardStatus(any()) } returns flowOf(AsyncResult.Success(mockk()))
        }

        val getCardStatusUseCase: GetCardStatusUseCase = GetCardStatusUseCaseImpl(repository)

        /* When */
        val cardStatus = getCardStatusUseCase(123412341234L).last()

        /* Then */
        assert(cardStatus is AsyncResult.Success<*>)
    }

    @Test
    fun `GetCardStatusUseCase returns an error when the repository returns an error`() = runBlocking {
        /* Given */
        val repository = mockk<CardRepository> {
            coEvery { getCardStatus(any()) } returns flowOf(AsyncResult.Error(mockk()))
        }

        val getCardStatusUseCase: GetCardStatusUseCase = GetCardStatusUseCaseImpl(repository)

        /* When */
        val cardStatus = getCardStatusUseCase(123412341234L).last()

        /* Then */
        assert(cardStatus is AsyncResult.Error<*>)
    }

}