package com.marcelorcorrea.pregnancycalculator.util

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

object TimeProvider {
    private var clock: Clock = Clock.systemDefaultZone()
    private val zoneId = ZoneId.systemDefault()

    fun now(): LocalDate {
        return LocalDate.now(clock)
    }

    fun useFixedClockAt(date: LocalDate) {
        clock = Clock.fixed(date.atStartOfDay(zoneId).toInstant(), zoneId)
    }
}
