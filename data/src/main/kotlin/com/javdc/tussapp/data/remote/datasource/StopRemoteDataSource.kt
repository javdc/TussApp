package com.javdc.tussapp.data.remote.datasource

import com.javdc.tussapp.common.util.serverDateTimeFormatter
import com.javdc.tussapp.data.remote.api.StopService
import com.javdc.tussapp.data.remote.mapper.toBo
import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import java.time.LocalDateTime

interface StopRemoteDataSource {
    suspend fun getStopEstimates(stopCode: Int): StopEstimatesResponseBo
    suspend fun getStops(version: Int): List<StopBo>
    suspend fun getLines(date: LocalDateTime): LinesResponseBo
    suspend fun getLineDirectionStops(lineId: Int, direction: Int, date: LocalDateTime): List<LineStopBo>
}

class StopRemoteDataSourceImpl(
    private val stopService: StopService
) : StopRemoteDataSource {

    override suspend fun getStopEstimates(stopCode: Int): StopEstimatesResponseBo =
        stopService
            .getStopEstimates(stopCode)
            .toBo()

    override suspend fun getStops(version: Int): List<StopBo> =
        stopService
            .getStops(version)
            .map { it.toBo() }

    override suspend fun getLines(date: LocalDateTime): LinesResponseBo =
        stopService
            .getLines(date.format(serverDateTimeFormatter))
            .toBo()

    override suspend fun getLineDirectionStops(lineId: Int, direction: Int, date: LocalDateTime): List<LineStopBo> =
        stopService
            .getLineStops(lineId, direction, date.format(serverDateTimeFormatter))
            .map { it.toBo() }

}