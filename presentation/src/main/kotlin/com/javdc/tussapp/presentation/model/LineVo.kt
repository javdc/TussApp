package com.javdc.tussapp.presentation.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineVo(
    val id: Int,
    val label: String?,
    @ColorInt val color: Int,
    val description: String?,
    val schedule: String?,
    val destinations: List<DestinationVo>
) : Parcelable