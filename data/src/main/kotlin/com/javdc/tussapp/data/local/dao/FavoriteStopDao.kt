package com.javdc.tussapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.javdc.tussapp.data.local.model.FavoriteStopDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStopDao {

    @Query("SELECT * FROM favorite_stop")
    fun getFavoriteStops(): Flow<List<FavoriteStopDbo>>

    @Query("SELECT * FROM favorite_stop WHERE stop_code = :code")
    suspend fun getFavoriteStop(code: Int): FavoriteStopDbo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteStop(favoriteStop: FavoriteStopDbo)

    @Query("DELETE FROM favorite_stop WHERE stop_code = :code")
    suspend fun removeFavoriteStop(code: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorite_stop WHERE stop_code = :code)")
     fun isStopFavorite(code: Int) : Flow<Boolean>

}