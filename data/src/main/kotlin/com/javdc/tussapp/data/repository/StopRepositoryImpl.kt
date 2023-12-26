package com.javdc.tussapp.data.repository

import com.javdc.tussapp.data.local.datasource.StopLocalDataSource
import com.javdc.tussapp.data.remote.datasource.StopRemoteDataSource
import com.javdc.tussapp.data.repository.util.RepositoryErrorManager
import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import java.time.LocalDateTime

class StopRepositoryImpl(
    private val stopRemoteDataSource: StopRemoteDataSource,
    private val stopLocalDataSource: StopLocalDataSource,
): StopRepository {

    override suspend fun getStopEstimates(stopCode: Int): Flow<AsyncResult<StopEstimatesResponseBo>> =
        RepositoryErrorManager.wrap {
            stopRemoteDataSource.getStopEstimates(stopCode)
        }

    override suspend fun getStops(version: Int): Flow<AsyncResult<List<StopBo>>> =
        RepositoryErrorManager.wrap {
            stopRemoteDataSource.getStops(version)
        }

    override suspend fun getLines(date: LocalDateTime): Flow<AsyncResult<LinesResponseBo>> =
        RepositoryErrorManager.wrap {
            stopRemoteDataSource.getLines(date)
        }

    override suspend fun getLineDirectionStops(lineId: Int, direction: Int, date: LocalDateTime): Flow<AsyncResult<List<LineStopBo>>> =
        RepositoryErrorManager.wrap {
            stopRemoteDataSource.getLineDirectionStops(lineId, direction, date)
        }

    override suspend fun getStopsWithLinesFromLocal(): Flow<List<StopBo>> =
        stopLocalDataSource.getStopsWithLines()

    override suspend fun getStopsSyncedVersion(): Int? {
        return stopLocalDataSource.getStopsSyncVersion()
    }

    override suspend fun syncStopsInLocal(version: Int): AsyncResult<Unit> {
        val result = getStops(version).last()
        return if (result is AsyncResult.Success) {
            if (result.data.isNotEmpty()) {
                stopLocalDataSource.deleteAllAndInsertStopsWithLines(result.data)
                stopLocalDataSource.saveStopsSyncVersion(version)
                AsyncResult.Success(Unit)
            } else {
                AsyncResult.Error(AsyncError.EmptyResponseError)
            }
        } else {
            AsyncResult.Error((result as? AsyncResult.Error)?.error ?: AsyncError.UnknownError())
        }
    }

}