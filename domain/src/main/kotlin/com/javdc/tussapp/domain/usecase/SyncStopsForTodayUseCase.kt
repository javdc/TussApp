package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.common.util.serverZoneId
import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import java.time.LocalDate

interface SyncStopsForTodayUseCase {
    suspend operator fun invoke(): AsyncResult<Unit>
}

class SyncStopsForTodayUseCaseImpl(
    private val stopRepository: StopRepository,
    private val dateRepository: DateRepository,
) : SyncStopsForTodayUseCase {

    override suspend operator fun invoke(): AsyncResult<Unit> {
        val today = LocalDate.now(serverZoneId)
        val todayVersion = dateRepository.getVersion(today)
        val syncedVersion = stopRepository.getStopsSyncedVersion()

        return if (todayVersion == null) {
            AsyncResult.Error(NoVersionForTodayError())
        } else if (syncedVersion == null || todayVersion != syncedVersion) {
            stopRepository.syncStopsInLocal(todayVersion)
        } else {
            AsyncResult.Success(Unit)
        }
    }

}

class NoVersionForTodayError : AsyncError.CustomError()