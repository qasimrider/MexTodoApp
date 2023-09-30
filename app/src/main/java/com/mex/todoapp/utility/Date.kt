package com.mex.todoapp.utility

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertMillisToDate(millis: Long, timeZone: String = "UTC"): LocalDate {
    val instant = Instant.ofEpochMilli(millis)
    val zoneId = ZoneId.of(timeZone)
    return instant.atZone(zoneId).toLocalDate()
}

fun LocalDate.format(): String = this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))