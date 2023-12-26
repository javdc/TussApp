package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class CardDto(
    @SerializedName("numeroSerie") val serialNumber: Long,
    @SerializedName("codigoTitulo") val passCode: Int,
    @SerializedName("nombreTitulo") val passName: String?,
    @SerializedName("ultimaOperacion") val lastOperation: String?,
    @SerializedName("inicioValidez") val validFrom: String?,
    @SerializedName("finValidez") val validTo: String?,
    @SerializedName("inicioAmpliacion") val extensionFrom: String?,
    @SerializedName("finAmpliacion") val extensionTo: String?,
    @SerializedName("saldoMonedero") val moneyBalance: Int?,
    @SerializedName("saldoViajes") val tripsBalance: Int?,
    @SerializedName("codigoResultado") val resultCode: Int?,
    @SerializedName("codigoAlerta") val alertCode: Int?,
    @SerializedName("caducidad") val expiry: String?,
)