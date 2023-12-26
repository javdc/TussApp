package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class DateVersionDto(
    @SerializedName("fecha") val date: String?,
    @SerializedName("version") val version: Int?,
)