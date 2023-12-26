package com.javdc.tussapp.data.local.datasource

import android.content.SharedPreferences
import com.javdc.tussapp.data.local.dao.StopDao
import com.javdc.tussapp.data.local.mapper.toBo
import com.javdc.tussapp.data.local.mapper.toDbo
import com.javdc.tussapp.data.local.model.StopLineCrossReferenceDbo
import com.javdc.tussapp.domain.model.StopBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface StopLocalDataSource {
    suspend fun getStopsWithLines(): Flow<List<StopBo>>
    suspend fun deleteAllAndInsertStopsWithLines(stops: List<StopBo>)
    suspend fun getStopsSyncVersion(): Int?
    suspend fun saveStopsSyncVersion(version: Int)
}

class StopLocalDataSourceImpl(
    private val stopDao: StopDao,
    private val sharedPreferences: SharedPreferences,
) : StopLocalDataSource {

    override suspend fun getStopsWithLines(): Flow<List<StopBo>> =
        stopDao.getStopsWithLines().map { list -> list.map { it.toBo() } }

    override suspend fun deleteAllAndInsertStopsWithLines(stops: List<StopBo>) {
        val lines = stops.flatMap { it.lines }.distinctBy { it.id }
        val stopLinesCrossReferences = stops.flatMap { stop ->
            stop.lines.map { line ->
                StopLineCrossReferenceDbo(stop.code, line.id)
            }
        }
        stopDao.deleteAllAndInsertStopsWithLines(
            stops = stops.map { it.toDbo() },
            lines = lines.map { it.toDbo() },
            stopLineCrossReferences = stopLinesCrossReferences,
        )
    }

    override suspend fun getStopsSyncVersion() =
        sharedPreferences.getInt(KEY_SYNCED_VERSION, -1).takeUnless { it == -1 }

    override suspend fun saveStopsSyncVersion(version: Int) =
        sharedPreferences.edit().putInt(KEY_SYNCED_VERSION, version).apply()

    private companion object {
        private const val KEY_SYNCED_VERSION = "synced_version"
    }

}