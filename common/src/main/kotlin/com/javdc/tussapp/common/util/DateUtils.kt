package com.javdc.tussapp.common.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val serverZoneId: ZoneId = ZoneId.of("Europe/Madrid")
val serverDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss")
val serverDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun LocalDate.toEpochMilliseconds(timeZone: ZoneId = serverZoneId): Long {
    return this.atStartOfDay().toEpochMilliseconds(timeZone)
}

fun LocalDateTime.toEpochMilliseconds(timeZone: ZoneId = serverZoneId): Long {
    return this.atZone(timeZone).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(timeZone: ZoneId = serverZoneId): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(timeZone).toLocalDateTime()
}
