package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class TextWithFormatDto(
    @SerializedName("texto") val text: String?,
    @SerializedName("formato") val format: List<String>?,
    @SerializedName("iconos") val icons: List<String>?,
)