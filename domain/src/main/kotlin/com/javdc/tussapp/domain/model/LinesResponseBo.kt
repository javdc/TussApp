package com.javdc.tussapp.domain.model

import java.time.LocalDateTime

data class LinesResponseBo(
    val date: LocalDateTime?,
    val availableLines: List<LineBo>,
)

data class LineBo(
    val id: Int,
    val label: String?,
    val subline: Int?,
    val description: String?,
    val color: String?,
    val destinations: List<DestinationBo>,
)

data class DestinationBo(
    val direction: Int,
    val destinationName: String?,
    val startTime: String?,
    val endTime: String?,
)