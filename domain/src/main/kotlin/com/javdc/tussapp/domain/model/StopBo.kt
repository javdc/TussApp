package com.javdc.tussapp.domain.model

data class StopBo(
    val code: Int,
    val description: String?,
    val lines: List<StopLineBo>,
)

data class StopLineBo(
    val id: Int,
    val label: String?,
    val color: String?,
)