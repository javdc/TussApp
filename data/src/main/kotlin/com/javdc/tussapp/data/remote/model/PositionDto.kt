package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class PositionDto(
    @SerializedName("latitudE6") val lat: Long?,
    @SerializedName("longitudE6") val long: Long?,
)