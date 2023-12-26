package com.javdc.tussapp.presentation.model

import androidx.annotation.ColorInt

data class CardVo(
    val cardNumber: Long,
    val customName: String?,
    val passName: String?,
    val expiry: String?,
    @ColorInt val primaryColorInt: Int,
    @ColorInt val secondaryColorInt: Int,
    val eurosBalance: String?,
    val tripsBalance: Int?,
)