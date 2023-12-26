package com.javdc.tussapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.javdc.tussapp.data.local.model.DateVersionDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface DateVersionDao {

    @Query("SELECT * FROM date_version")
    suspend fun getDateVersions(): List<DateVersionDbo>

    @Query("SELECT * FROM date_version")
    fun getDateVersionsFlow(): Flow<List<DateVersionDbo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dateVersions: List<DateVersionDbo>)

    @Query("DELETE FROM date_version")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllAndInsert(dateVersions: List<DateVersionDbo>) {
        deleteAll()
        insert(dateVersions)
    }

    @Query("SELECT * FROM date_version WHERE date = :date")
    suspend fun getDateVersion(date: String) : DateVersionDbo?

    @Query("SELECT version FROM date_version WHERE date = :date")
    suspend fun getVersion(date: String) : Int?

}