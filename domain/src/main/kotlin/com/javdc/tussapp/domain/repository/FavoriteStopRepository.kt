package com.javdc.tussapp.domain.repository

import com.javdc.tussapp.domain.model.FavoriteStopBo
import kotlinx.coroutines.flow.Flow

interface FavoriteStopRepository {
    suspend fun getFavoriteStops(): Flow<List<FavoriteStopBo>>
    suspend fun addStopToFavorites(code: Int)
    suspend fun isStopFavorite(code: Int): Flow<Boolean>
    suspend fun removeStopFromFavorites(code: Int)
}