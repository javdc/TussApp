package com.javdc.tussapp.presentation.model

import java.time.LocalDateTime

data class LinesInfoVo(
    val date: LocalDateTime?,
    val availableLines: List<LineVo>,
)