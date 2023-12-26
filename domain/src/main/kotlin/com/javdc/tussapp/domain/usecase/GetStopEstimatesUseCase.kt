package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

interface GetStopEstimatesUseCase {
    suspend operator fun invoke(stopCode: Int): Flow<AsyncResult<StopEstimatesResponseBo>>
}

class GetStopEstimatesUseCaseImpl(private val repository: StopRepository) : GetStopEstimatesUseCase {
    override suspend operator fun invoke(stopCode: Int): Flow<AsyncResult<StopEstimatesResponseBo>> {
        return repository.getStopEstimates(stopCode)
    }

}