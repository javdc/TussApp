package com.javdc.tussapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.javdc.tussapp.data.local.model.LineDbo
import com.javdc.tussapp.data.local.model.StopDbo
import com.javdc.tussapp.data.local.model.StopLineCrossReferenceDbo
import com.javdc.tussapp.data.local.model.StopWithLinesDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {

    @Transaction
    @Query("SELECT * FROM stop")
    fun getStopsWithLines(): Flow<List<StopWithLinesDbo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<StopDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(lines: List<LineDbo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStopLineCrossReferences(stopLineCrossReferences: List<StopLineCrossReferenceDbo>)

    @Query("DELETE FROM stop")
    suspend fun deleteAllStops()

    @Query("DELETE FROM line")
    suspend fun deleteAllLines()

    @Query("DELETE FROM stop_line_cross_ref")
    suspend fun deleteAllStopLineCrossReferences()

    @Transaction
    suspend fun deleteAllAndInsertStopsWithLines(stops: List<StopDbo>, lines: List<LineDbo>, stopLineCrossReferences: List<StopLineCrossReferenceDbo>) {
        deleteAllStops()
        deleteAllLines()
        deleteAllStopLineCrossReferences()
        insertStops(stops)
        insertLines(lines)
        insertStopLineCrossReferences(stopLineCrossReferences)
    }

}