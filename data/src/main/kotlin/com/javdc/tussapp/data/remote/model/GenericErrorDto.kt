package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class GenericErrorDto(
    @SerializedName("estado") val status: String,
    @SerializedName("mensaje") val message: String,
    @SerializedName("detalles") val details: List<String>?,
)