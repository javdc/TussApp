package com.javdc.tussapp.data.remote.api

import com.javdc.tussapp.data.remote.model.DateVersionDto
import retrofit2.http.GET

interface DateService {

    @GET("/INFOTUS/api/v2/fechas")
    suspend fun getDates(): List<DateVersionDto>

}