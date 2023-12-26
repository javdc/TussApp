package com.javdc.tussapp.domain.model

import java.time.LocalTime

data class LineStopBo(
    val code: Int,
    val description: String?,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
)