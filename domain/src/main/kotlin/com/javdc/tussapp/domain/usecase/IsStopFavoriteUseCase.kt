package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.FavoriteStopRepository
import kotlinx.coroutines.flow.Flow

interface IsStopFavoriteUseCase {
    suspend operator fun invoke(code: Int): Flow<Boolean>
}

class IsStopFavoriteUseCaseImpl(private val repository: FavoriteStopRepository) : IsStopFavoriteUseCase {
    override suspend operator fun invoke(code: Int): Flow<Boolean> {
        return repository.isStopFavorite(code)
    }

}