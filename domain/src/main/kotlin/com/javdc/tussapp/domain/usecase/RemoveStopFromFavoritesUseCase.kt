package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.FavoriteStopRepository

interface RemoveStopFromFavoritesUseCase {
    suspend operator fun invoke(code: Int)
}

class RemoveStopFromFavoritesUseCaseImpl(private val repository: FavoriteStopRepository) :
    RemoveStopFromFavoritesUseCase {
    override suspend operator fun invoke(code: Int) {
        return repository.removeStopFromFavorites(code)
    }

}