package com.javdc.tussapp.presentation.model

data class StopEstimatesInfoVo(
    val description: String?,
    val latLng: Pair<Double, Double>?,
    val estimates: List<StopEstimateVo>,
)