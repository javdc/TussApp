package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class StopDto(
    @SerializedName("codigo") val code: Int,
    @SerializedName("descripcion") val description: TextWithFormatDto?,
    @SerializedName("posicion") val position: PositionDto?,
    @SerializedName("lineasCoincidentes") val lines: List<StopLineDto>?,
)

data class StopLineDto(
    @SerializedName("linea") val id: Int,
    @SerializedName("labelLinea") val label: String?,
    @SerializedName("color") val color: String?,
)