package com.javdc.tussapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class NoticesResponseDto(
    @SerializedName("numeroAvisos") val numberOfNotices: Int?,
    @SerializedName("avisos") val notices: List<NoticeDto>?,
    @SerializedName("status") val status: String?, // Unused in official app
    @SerializedName("error") val error: String?, // Unused in official app
)

data class NoticeDto(
    @SerializedName("avisoId") val noticeId: Int,
    @SerializedName("titulo") val title: String?,
    @SerializedName("descripcion") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("lineas") val lines: List<NoticeLineDto>?,
    @SerializedName("paradas") val stops: List<NoticeStopDto>?,
    @SerializedName("fechaInicio") val startDate: Long?,
    @SerializedName("fechaFin") val endDate: Long?, // Unused in official app
    @SerializedName("categoria") val category: Int?, // Unused in official app
    @SerializedName("enviarComoPush") val sendAsPush: Int?, // Unused in official app
    @SerializedName("emailComoEmail") val emailAsEmail: Int?, // Unused in official app
    @SerializedName("imagen") val imageBase64: String?,
    @SerializedName("tipoImagen") val imageType: Int?, // 1 small image, else big image
    @SerializedName("orden") val order: Int?,
    @SerializedName("mostrarEnLaHome") val showInHome: Int?, // true if != 0
)

data class NoticeLineDto(
    @SerializedName("lineaId") val lineId: Int?,
    @SerializedName("nombre") val name: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("color") val color: String?,
)

data class NoticeStopDto(
    @SerializedName("paradaId") val stopId: Int?,
    @SerializedName("nombre") val name: String?,
)