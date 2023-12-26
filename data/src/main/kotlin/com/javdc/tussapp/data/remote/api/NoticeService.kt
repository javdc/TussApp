package com.javdc.tussapp.data.remote.api

import com.javdc.tussapp.data.remote.model.NoticesResponseDto
import retrofit2.http.GET

interface NoticeService {

    @GET("/appTussam-ws/ws/avisos/getAll")
    suspend fun getNotices(): NoticesResponseDto

}