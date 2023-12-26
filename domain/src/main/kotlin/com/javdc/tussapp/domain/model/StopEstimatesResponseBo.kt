package com.javdc.tussapp.domain.model

data class StopEstimatesResponseBo(
    val code: Int,
    val description: String?,
    val latLng: Pair<Double, Double>?,
    val matchingLines: List<LineWithEstimatesBo>,
)

data class LineWithEstimatesBo(
    val id: Int,
    val label: String?,
    val color: String?,
    val estimates: List<EstimateBo>,
)

data class EstimateBo(
    val vehicle: Int,
    val destination: String?,
    val distance: Int?,
    val seconds: Int?,
    val isLast: Boolean,
)