package com.javdc.tussapp.domain.model

import java.time.LocalDate

data class DateVersionBo(
    val date: LocalDate,
    val version: Int,
)