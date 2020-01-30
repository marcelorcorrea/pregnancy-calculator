package com.marcelorcorrea.pregnancycalculator.service

import com.marcelorcorrea.pregnancycalculator.model.GestationalAge
import com.marcelorcorrea.pregnancycalculator.util.TimeProvider
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

class PregnancyCalculatorService {
    companion object {
        private const val WEEKS_OF_GESTATION: Long = 38
        private const val TOTAL_WEEKS_OF_GESTATION: Long = 40
        private const val LAST_MENSTRUAL_PERIOD_WEEKS: Long = 2
        private val TODAY = TimeProvider.now()
        private val MAXIMUM_DUE_DATE = TODAY.plusWeeks(TOTAL_WEEKS_OF_GESTATION)
        private val MINIMUM_DUE_DATE = TODAY
        private val MAXIMUM_DATE_OF_CONCEPTION = TODAY.minusWeeks(40)
        private val MINIMUM_DATE_OF_CONCEPTION = TODAY
    }

    fun calculatePeriodFrom(weeks: Long): Period {
        val possibleConceptionDate = TODAY.minusWeeks(weeks)
        return Period.between(possibleConceptionDate, TODAY)
    }

    fun calculateDateOfConception(dueDate: LocalDate): LocalDate {
        return dueDate.minusWeeks(WEEKS_OF_GESTATION)
    }

    fun calculateDateOfLastMenstrualPeriod(dateOfConception: LocalDate): LocalDate {
        return dateOfConception.minusWeeks(LAST_MENSTRUAL_PERIOD_WEEKS)
    }

    fun calculateGestationalAge(lastMenstrualPeriod: LocalDate): GestationalAge {
        val diffInDays = ChronoUnit.DAYS.between(lastMenstrualPeriod, TODAY)
        val weeks = (diffInDays / 7)
        val days = diffInDays % 7
        return GestationalAge(weeks.toInt(), days.toInt())
    }

    fun calculateDueDate(lastMenstrualPeriod: LocalDate): LocalDate {
        return lastMenstrualPeriod.plusWeeks(TOTAL_WEEKS_OF_GESTATION)
    }

    fun isDueDateExceedsLimit(dueDate: LocalDate): Boolean {
        return dueDate.isAfter(MAXIMUM_DUE_DATE) || dueDate.isBefore(MINIMUM_DUE_DATE)
    }

    fun isLastMenstrualPeriodExceedsLimit(lastMenstrualPeriod: LocalDate): Boolean {
        return lastMenstrualPeriod.isAfter(MINIMUM_DATE_OF_CONCEPTION) || lastMenstrualPeriod.isBefore(
            MAXIMUM_DATE_OF_CONCEPTION
        )
    }
}
