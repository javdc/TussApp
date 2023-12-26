package com.javdc.tussapp.data.repository

import com.javdc.tussapp.data.local.datasource.FavoriteStopLocalDataSource
import com.javdc.tussapp.domain.model.FavoriteStopBo
import com.javdc.tussapp.domain.repository.FavoriteStopRepository
import kotlinx.coroutines.flow.Flow

class FavoriteStopRepositoryImpl(
    private val favoriteStopLocalDataSource: FavoriteStopLocalDataSource,
): FavoriteStopRepository {

    override suspend fun getFavoriteStops(): Flow<List<FavoriteStopBo>> =
        favoriteStopLocalDataSource.getFavoriteStops()

    override suspend fun addStopToFavorites(code: Int) =
        favoriteStopLocalDataSource.addStopToFavorites(code)

    override suspend fun isStopFavorite(code: Int) =
        favoriteStopLocalDataSource.isStopFavorite(code)

    override suspend fun removeStopFromFavorites(code: Int) =
        favoriteStopLocalDataSource.removeStopFromFavorites(code)

}