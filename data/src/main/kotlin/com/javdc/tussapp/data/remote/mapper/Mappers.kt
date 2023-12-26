package com.javdc.tussapp.data.remote.mapper

import com.javdc.tussapp.common.util.safeLet
import com.javdc.tussapp.common.util.serverDateFormatter
import com.javdc.tussapp.common.util.serverDateTimeFormatter
import com.javdc.tussapp.common.util.toLocalDateTime
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.data.remote.model.LineDto
import com.javdc.tussapp.data.remote.model.CardDto
import com.javdc.tussapp.data.remote.model.DateVersionDto
import com.javdc.tussapp.data.remote.model.DestinationDto
import com.javdc.tussapp.data.remote.model.EstimateDto
import com.javdc.tussapp.data.remote.model.LineStopDto
import com.javdc.tussapp.data.remote.model.LinesResponseDto
import com.javdc.tussapp.data.remote.model.LineWithEstimatesDto
import com.javdc.tussapp.data.remote.model.NoticeDto
import com.javdc.tussapp.data.remote.model.NoticeLineDto
import com.javdc.tussapp.data.remote.model.NoticeStopDto
import com.javdc.tussapp.data.remote.model.NoticesResponseDto
import com.javdc.tussapp.data.remote.model.PositionDto
import com.javdc.tussapp.data.remote.model.StopDto
import com.javdc.tussapp.data.remote.model.StopEstimatesResponseDto
import com.javdc.tussapp.data.remote.model.StopLineDto
import com.javdc.tussapp.domain.model.LineBo
import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.model.CardTypeBo
import com.javdc.tussapp.domain.model.DateVersionBo
import com.javdc.tussapp.domain.model.DestinationBo
import com.javdc.tussapp.domain.model.EstimateBo
import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.model.LineWithEstimatesBo
import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.domain.model.NoticeLineBo
import com.javdc.tussapp.domain.model.NoticeStopBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import com.javdc.tussapp.domain.model.StopLineBo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun NoticesResponseDto.toBo(): List<NoticeBo> =
    notices?.map { it.toBo() }.orEmpty()

fun NoticeDto.toBo() = NoticeBo(
    noticeId = noticeId,
    title = title,
    description = description,
    url = url,
    lines = lines?.map { it.toBo() }.orEmpty(),
    stops = stops?.map { it.toBo() }.orEmpty(),
    startDate = tryOrNull { startDate?.toLocalDateTime() },
    endDate = tryOrNull { endDate?.toLocalDateTime() },
    imageBase64 = imageBase64,
    bigImage = imageType != 1 && !imageBase64.isNullOrBlank(),
    showInHome = showInHome != 0,
)

fun NoticeLineDto.toBo() = NoticeLineBo(
    lineId = lineId,
    name = name,
    label = label,
    color = color,
)

fun NoticeStopDto.toBo() = NoticeStopBo(
    stopId = stopId,
    name = name,
)

fun StopEstimatesResponseDto.toBo() = StopEstimatesResponseBo(
    code = code,
    description = description?.text,
    latLng = position?.toBo(),
    matchingLines = matchingLines?.map { it.toBo() }.orEmpty(),
)

fun PositionDto.toBo() = safeLet(lat, long) { lat, long ->
    Pair(lat/1000000.toDouble(), long/1000000.toDouble())
}

fun LineWithEstimatesDto.toBo() = LineWithEstimatesBo(
    id = id,
    label = label,
    color = color,
    estimates = estimates?.map { it.toBo() }?.sortedBy { it.seconds }.orEmpty(),
)

fun EstimateDto.toBo() = EstimateBo(
    vehicle = vehicle,
    destination = destination?.text?.removeSuffix(" - ÚLTIMO BUS")?.removeSuffix(".")?.trim(),
    distance = distance,
    seconds = seconds,
    isLast = attributes.contains("ultimo_bus")
)

fun CardDto.toBo(cardNumber: Long?) = CardBo(
    cardNumber = cardNumber ?: serialNumber,
    customName = null,
    serialNumber = serialNumber,
    cardType = CardTypeBo.getByCode(passCode),
    passName = passName,
    lastOperation = lastOperation,
    validFrom = validFrom,
    validTo = validTo,
    extensionFrom = extensionFrom,
    extensionTo = extensionTo,
    eurosBalance = moneyBalance?.toFloat()?.div(1000),
    tripsBalance = tripsBalance,
    resultCode = resultCode,
    alertCode = alertCode,
    expiry = expiry?.takeIf { it.isNotBlank() }?.takeIf { it != "31/12/2063" },
)

fun StopDto.toBo() = StopBo(
    code = code,
    description = description?.text,
    lines = lines?.map { it.toBo() }.orEmpty()
)

fun StopLineDto.toBo() = StopLineBo(
    id = id,
    label = label,
    color = color,
)

fun LinesResponseDto.toBo() = LinesResponseBo(
    date = tryOrNull { LocalDateTime.parse(date, serverDateTimeFormatter) },
    availableLines = availableLines?.map { it.toBo() }.orEmpty(),
)

fun LineDto.toBo() = LineBo(
    id = id,
    label = label,
    subline = subline,
    description = description?.text,
    color = color,
    destinations = destinations?.map { it.toBo() }.orEmpty(),
)

fun DestinationDto.toBo() = DestinationBo(
    direction = direction,
    destinationName = destination?.text,
    startTime = startTime,
    endTime = endTime,
)

fun List<DateVersionDto>.toBo(): List<DateVersionBo> =
    mapNotNull { it.toBo() }

fun DateVersionDto.toBo(): DateVersionBo? =
    tryOrNull { LocalDate.parse(date, serverDateFormatter)?.let { DateVersionBo(it, version ?: -1) } }

fun LineStopDto.toBo() = LineStopBo(
    code = code,
    description = description?.text,
    startTime = tryOrNull { startTime?.takeIf { it != "-" }?.let { LocalTime.parse(it) } },
    endTime = tryOrNull { endTime?.takeIf { it != "-" }?.let { LocalTime.parse(it) } },
)