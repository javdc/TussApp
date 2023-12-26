package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class LineStopDto(
    @SerializedName("codigo") val code: Int,
    @SerializedName("descripcion") val description: TextWithFormatDto?,
    @SerializedName("posicion") val position: PositionDto?,
    @SerializedName("distancia") val distance: Int?,
    @SerializedName("horaInicio") val startTime: String?,
    @SerializedName("horaFin") val endTime: String?,
)