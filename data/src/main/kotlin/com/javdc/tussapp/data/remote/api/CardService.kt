package com.javdc.tussapp.data.remote.api

import com.javdc.tussapp.data.remote.model.CardDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CardService {

    @GET("/INFOTUS/api/v2/estadoTarjeta/{cardNumber}")
    suspend fun getCardStatus(@Path("cardNumber") cardNumber: Long): CardDto

}