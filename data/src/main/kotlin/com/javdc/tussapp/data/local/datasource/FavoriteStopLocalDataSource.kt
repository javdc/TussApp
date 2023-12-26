package com.javdc.tussapp.data.local.datasource

import com.javdc.tussapp.data.local.dao.FavoriteStopDao
import com.javdc.tussapp.data.local.mapper.toBo
import com.javdc.tussapp.data.local.model.FavoriteStopDbo
import com.javdc.tussapp.domain.model.FavoriteStopBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavoriteStopLocalDataSource {
    suspend fun getFavoriteStops(): Flow<List<FavoriteStopBo>>
    suspend fun isStopFavorite(code: Int): Flow<Boolean>
    suspend fun addStopToFavorites(code: Int)
    suspend fun removeStopFromFavorites(code: Int)
}

class FavoriteStopLocalDataSourceImpl(
    private val favoriteStopDao: FavoriteStopDao,
) : FavoriteStopLocalDataSource {

    override suspend fun getFavoriteStops(): Flow<List<FavoriteStopBo>> {
        return favoriteStopDao.getFavoriteStops().map { list -> list.map { it.toBo() } }
    }

    override suspend fun isStopFavorite(code: Int): Flow<Boolean> {
        return favoriteStopDao.isStopFavorite(code)
    }

    override suspend fun addStopToFavorites(code: Int) {
        favoriteStopDao.insertFavoriteStop(FavoriteStopDbo(code))
    }

    override suspend fun removeStopFromFavorites(code: Int) {
        favoriteStopDao.removeFavoriteStop(code)
    }

}
