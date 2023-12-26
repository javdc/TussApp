package com.javdc.tussapp.data.remote.api

import com.javdc.tussapp.data.remote.model.LineStopDto
import com.javdc.tussapp.data.remote.model.LinesResponseDto
import com.javdc.tussapp.data.remote.model.StopDto
import com.javdc.tussapp.data.remote.model.StopEstimatesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StopService {

    @GET("/INFOTUS/api/v2/tiempos/{stopCode}")
    suspend fun getStopEstimates(@Path("stopCode") stopCode: Int): StopEstimatesResponseDto

    @GET("/INFOTUS/api/v2/nodos/{version}")
    suspend fun getStops(@Path("version") version: Int): List<StopDto>

    @GET("/INFOTUS/api/v2/lineas")
    suspend fun getLines(@Query("fechaConcreta") date: String): LinesResponseDto

    @GET("/INFOTUS/api/v2/nodosLinea/{lineId}")
    suspend fun getLineStops(@Path("lineId") lineId: Int, @Query("sentido") direction: Int, @Query("fecha") date: String): List<LineStopDto>

}