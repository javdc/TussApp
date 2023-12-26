package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.FavoriteStopBo
import com.javdc.tussapp.domain.repository.FavoriteStopRepository
import kotlinx.coroutines.flow.Flow

interface GetFavoriteStopsUseCase {
    suspend operator fun invoke(): Flow<List<FavoriteStopBo>>
}

class GetFavoriteStopsUseCaseImpl(private val repository: FavoriteStopRepository) : GetFavoriteStopsUseCase {
    override suspend operator fun invoke(): Flow<List<FavoriteStopBo>> {
        return repository.getFavoriteStops()
    }

}