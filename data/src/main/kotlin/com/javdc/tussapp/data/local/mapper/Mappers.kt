package com.javdc.tussapp.data.local.mapper

import com.javdc.tussapp.data.local.model.CardDbo
import com.javdc.tussapp.data.local.model.CardUpdateFromRemoteDbo
import com.javdc.tussapp.data.local.model.DateVersionDbo
import com.javdc.tussapp.data.local.model.FavoriteStopDbo
import com.javdc.tussapp.data.local.model.LineDbo
import com.javdc.tussapp.data.local.model.StopDbo
import com.javdc.tussapp.data.local.model.StopWithLinesDbo
import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.model.CardTypeBo
import com.javdc.tussapp.domain.model.DateVersionBo
import com.javdc.tussapp.domain.model.FavoriteStopBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.model.StopLineBo
import java.time.LocalDate

fun FavoriteStopDbo.toBo() = FavoriteStopBo(
    code = code,
)

fun StopWithLinesDbo.toBo() = StopBo(
    code = stop.code,
    description = stop.description,
    lines = lines.map { it.toBo() }
)

fun LineDbo.toBo() = StopLineBo(
    id = id,
    label = label,
    color = color,
)

fun StopBo.toDbo() = StopDbo(
    code = code,
    description = description,
)

fun StopLineBo.toDbo() = LineDbo(
    id = id,
    label = label,
    color = color,
)

fun CardBo.toDbo() = CardDbo(
    cardNumber = cardNumber,
    customName = customName,
    serialNumber = serialNumber,
    passCode = cardType?.passCode,
    passName = passName,
    lastOperation = lastOperation,
    validFrom = validFrom,
    validTo = validTo,
    extensionFrom = extensionFrom,
    extensionTo = extensionTo,
    eurosBalance = eurosBalance,
    tripsBalance = tripsBalance,
    resultCode = resultCode,
    alertCode = alertCode,
    expiry = expiry,
)

fun CardBo.toUpdateFromRemoteDbo() = CardUpdateFromRemoteDbo(
    cardNumber = cardNumber,
    serialNumber = serialNumber,
    passCode = cardType?.passCode,
    passName = passName,
    lastOperation = lastOperation,
    validFrom = validFrom,
    validTo = validTo,
    extensionFrom = extensionFrom,
    extensionTo = extensionTo,
    eurosBalance = eurosBalance,
    tripsBalance = tripsBalance,
    resultCode = resultCode,
    alertCode = alertCode,
    expiry = expiry,
)

fun CardDbo.toBo() = CardBo(
    cardNumber = cardNumber,
    customName = customName,
    serialNumber = serialNumber,
    cardType = passCode?.let { CardTypeBo.getByCode(it) },
    passName = passName,
    lastOperation = lastOperation,
    validFrom = validFrom,
    validTo = validTo,
    extensionFrom = extensionFrom,
    extensionTo = extensionTo,
    eurosBalance = eurosBalance,
    tripsBalance = tripsBalance,
    resultCode = resultCode,
    alertCode = alertCode,
    expiry = expiry,
)

fun DateVersionBo.toDbo() = DateVersionDbo(
    date = date.toString(),
    version = version,
)

fun DateVersionDbo.toBo() = DateVersionBo(
    date = LocalDate.parse(date),
    version = version,
)