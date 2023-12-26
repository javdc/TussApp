package com.javdc.tussapp.presentation.mapper

import android.graphics.Color
import androidx.core.graphics.toColorInt
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.domain.model.LineBo
import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.model.DestinationBo
import com.javdc.tussapp.domain.model.LinesResponseBo
import com.javdc.tussapp.domain.model.StopEstimatesResponseBo
import com.javdc.tussapp.presentation.model.LineVo
import com.javdc.tussapp.presentation.model.CardVo
import com.javdc.tussapp.presentation.model.DestinationVo
import com.javdc.tussapp.presentation.model.LinesInfoVo
import com.javdc.tussapp.presentation.model.StopEstimateVo
import com.javdc.tussapp.presentation.model.StopEstimatesInfoVo
import com.javdc.tussapp.presentation.util.toFormattedEurosString

fun LinesResponseBo.toVo() = LinesInfoVo(
    date = date,
    availableLines = availableLines.map { it.toVo() }
)

fun LineBo.toVo() = LineVo(
    id = id,
    label = label,
    color = tryOrNull { color?.toColorInt() } ?: Color.BLACK,
    description = description,
    schedule = destinations.firstOrNull()?.let {
        "${it.startTime} - ${it.endTime}"
    },
    destinations = destinations.map { it.toVo() },
)

fun DestinationBo.toVo() = DestinationVo(
    direction = direction,
    destinationName = destinationName,
)

fun CardBo.toVo() = CardVo(
    cardNumber = cardNumber,
    customName = customName,
    passName = passName,
    expiry = expiry,
    primaryColorInt = tryOrNull { cardType?.primaryColor?.toColorInt() } ?: "#a50034".toColorInt(),
    secondaryColorInt = tryOrNull { cardType?.secondaryColor?.toColorInt() } ?: "#ffffff".toColorInt(),
    eurosBalance = eurosBalance?.toFormattedEurosString(),
    tripsBalance = tripsBalance,
)

fun StopEstimatesResponseBo.toVo() = StopEstimatesInfoVo(
    description = description,
    latLng = latLng,
    estimates = matchingLines.flatMap { line ->
        val lineColor = tryOrNull { line.color?.toColorInt() } ?: Color.BLACK
        line.estimates.map { estimate ->
            StopEstimateVo(
                vehicle = estimate.vehicle,
                lineLabel = line.label,
                lineColor = lineColor,
                destination = estimate.destination,
                seconds = estimate.seconds,
            )
        }
    }.sortedBy { estimate ->
        estimate.seconds.takeUnless { it == -1800 } ?: Int.MAX_VALUE
    },
)