package com.javdc.tussapp.domain.repository

import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface StopRepository {
    suspend fun getStopEstimates(stopCode: Int): Flow<AsyncResult<StopEstimatesResponseBo>>
    suspend fun getStops(version: Int): Flow<AsyncResult<List<StopBo>>>
    suspend fun getLines(date: LocalDateTime): Flow<AsyncResult<LinesResponseBo>>
    suspend fun getLineDirectionStops(lineId: Int, direction: Int, date: LocalDateTime): Flow<AsyncResult<List<LineStopBo>>>
    suspend fun getStopsWithLinesFromLocal(): Flow<List<StopBo>>
    suspend fun getStopsSyncedVersion(): Int?
    suspend fun syncStopsInLocal(version: Int): AsyncResult<Unit>
}