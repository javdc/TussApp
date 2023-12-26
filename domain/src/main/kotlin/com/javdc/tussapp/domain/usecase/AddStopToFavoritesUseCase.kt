package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.FavoriteStopRepository

interface AddStopToFavoritesUseCase {
    suspend operator fun invoke(code: Int)
}

class AddStopToFavoritesUseCaseImpl(private val repository: FavoriteStopRepository) : AddStopToFavoritesUseCase {

    override suspend operator fun invoke(code: Int) {
        repository.addStopToFavorites(code)
    }

}