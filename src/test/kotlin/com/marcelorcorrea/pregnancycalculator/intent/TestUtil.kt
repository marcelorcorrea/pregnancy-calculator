package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.ui.PlainTextOutputSpeech
import com.marcelorcorrea.pregnancycalculator.model.GestationalAge
import com.marcelorcorrea.pregnancycalculator.util.I18nResource

object TestUtil {
    fun getOutputSpeechText(response: AlexaResponse) = ((response.response.outputSpeech) as PlainTextOutputSpeech).text

    fun makeMonthsPregnancyMessage(months: Int, days: Int, i18nResource: I18nResource): String {
        return if (months == 1 && days == 1) {
            i18nResource.getString("one_month_one_day")
        } else if (months == 1 && days > 1) {
            i18nResource.getString("one_month_days").format(days)
        } else if (months > 1 && days == 0) {
            i18nResource.getString("months").format(months)
        } else if (months == 1 && days == 0) {
            i18nResource.getString("month")
        } else if (months == 0 && days > 1) {
            i18nResource.getString("days").format(days)
        } else if (months == 0 && days == 1) {
            i18nResource.getString("one_day")
        } else {
            i18nResource.getString("months_and_days").format(months, days)
        }
    }

    fun makeWeeksPregnancyMessage(gestationalAge: GestationalAge, i18nResource: I18nResource): String {
        val weeks = gestationalAge.weeks
        val days = gestationalAge.days
        return if (weeks == 1 && days == 1) {
            i18nResource.getString("one_week_one_day")
        } else if (weeks == 1 && days > 1) {
            i18nResource.getString("one_week_days").format(days)
        } else if (weeks > 1 && days == 0) {
            i18nResource.getString("weeks").format(weeks)
        } else if (weeks == 1 && days == 0) {
            i18nResource.getString("one_week")
        } else if (weeks == 0 && days > 1) {
            i18nResource.getString("days").format(days)
        } else if (weeks == 0 && days == 1) {
            i18nResource.getString("one_day")
        } else {
            i18nResource.getString("weeks_and_days").format(weeks, days)
        }
    }
}
