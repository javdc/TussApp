package com.javdc.tussapp.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DestinationVo(
    val direction: Int,
    val destinationName: String?,
): Parcelable