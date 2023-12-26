package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetLineDirectionStopsUseCase {
    suspend operator fun invoke(lineId: Int, direction: Int, date: LocalDateTime): Flow<AsyncResult<List<LineStopBo>>>
}

class GetLineDirectionStopsUseCaseImpl(private val repository: StopRepository) : GetLineDirectionStopsUseCase {
    override suspend operator fun invoke(lineId: Int, direction: Int, date: LocalDateTime): Flow<AsyncResult<List<LineStopBo>>> {
        return repository.getLineDirectionStops(lineId, direction, date)
    }

}