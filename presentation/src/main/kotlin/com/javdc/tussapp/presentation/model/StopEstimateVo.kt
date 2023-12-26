package com.javdc.tussapp.presentation.model

import androidx.annotation.ColorInt

data class StopEstimateVo(
    val vehicle: Int,
    val lineLabel: String?,
    @ColorInt val lineColor: Int,
    val destination: String?,
    val seconds: Int?,
)