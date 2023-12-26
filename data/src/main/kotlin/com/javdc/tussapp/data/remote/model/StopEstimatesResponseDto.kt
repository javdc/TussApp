package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class StopEstimatesResponseDto(
    @SerializedName("codigo") val code: Int,
    @SerializedName("descripcion") val description: TextWithFormatDto?,
    @SerializedName("posicion") val position: PositionDto?,
    @SerializedName("lineasCoincidentes") val matchingLines: List<LineWithEstimatesDto>?,
)

data class LineWithEstimatesDto(
    @SerializedName("lineaId") val id: Int,
    @SerializedName("labelLinea") val label: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("estimaciones") val estimates: List<EstimateDto>?,
)

data class EstimateDto(
    @SerializedName("vehiculo") val vehicle: Int,
    @SerializedName("destino") val destination: TextWithFormatDto?,
    @SerializedName("distancia") val distance: Int?,
    @SerializedName("segundos") val seconds: Int?,
    @SerializedName("atributos") val attributes: List<String>,
)