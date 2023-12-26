package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class LinesResponseDto(
    @SerializedName("generadoPara") val date: String,
    @SerializedName("lineasDisponibles") val availableLines: List<LineDto>?,
)

data class LineDto(
    @SerializedName("linea") val id: Int,
    @SerializedName("labelLinea") val label: String?,
    @SerializedName("sublinea") val subline: Int?,
    @SerializedName("descripcion") val description: TextWithFormatDto?,
    @SerializedName("color") val color: String?,
    @SerializedName("destinos") val destinations: List<DestinationDto>?,
)

data class DestinationDto(
    @SerializedName("sentido") val direction: Int,
    @SerializedName("destino") val destination: TextWithFormatDto?,
    @SerializedName("horaInicio") val startTime: String?,
    @SerializedName("horaFin") val endTime: String?,
)