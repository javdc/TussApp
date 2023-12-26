package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.util.AsyncResult

interface SyncDateVersionsUseCase {
    suspend operator fun invoke(): AsyncResult<Unit>
}

class SyncDateVersionsUseCaseImpl(
    private val dateRepository: DateRepository,
) : SyncDateVersionsUseCase {

    override suspend operator fun invoke(): AsyncResult<Unit> {
        return dateRepository.syncDateVersionsInLocal()
    }

}