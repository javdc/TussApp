package com.javdc.tussapp.domain.model

import java.time.LocalDateTime

data class NoticeBo(
    val noticeId: Int,
    val title: String?,
    val description: String?,
    val url: String?,
    val lines: List<NoticeLineBo>,
    val stops: List<NoticeStopBo>,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val imageBase64: String?,
    val bigImage: Boolean,
    val showInHome: Boolean,
)

data class NoticeLineBo(
    val lineId: Int?,
    val name: String?,
    val label: String?,
    val color: String?,
)

data class NoticeStopBo(
    val stopId: Int?,
    val name: String?,
)