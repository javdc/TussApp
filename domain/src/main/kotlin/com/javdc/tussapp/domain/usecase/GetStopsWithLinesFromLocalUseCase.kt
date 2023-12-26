package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.repository.StopRepository
import kotlinx.coroutines.flow.Flow

interface GetStopsWithLinesFromLocalUseCase {
    suspend operator fun invoke(): Flow<List<StopBo>>
}

class GetStopsWithLinesFromLocalUseCaseImpl(private val repository: StopRepository) : GetStopsWithLinesFromLocalUseCase {
    override suspend operator fun invoke(): Flow<List<StopBo>> {
        return repository.getStopsWithLinesFromLocal()
    }

}