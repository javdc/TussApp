package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetLinesUseCase {
    suspend operator fun invoke(date: LocalDateTime): Flow<AsyncResult<LinesResponseBo>>
}

class GetLinesUseCaseImpl(private val repository: StopRepository) : GetLinesUseCase {
    override suspend operator fun invoke(date: LocalDateTime): Flow<AsyncResult<LinesResponseBo>> {
        return repository.getLines(date)
    }

}