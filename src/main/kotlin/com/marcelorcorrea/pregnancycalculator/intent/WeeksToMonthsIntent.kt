package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.annotation.Intent
import com.hp.kalexa.core.intent.IntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import com.marcelorcorrea.pregnancycalculator.util.Util.endInteractionWithSpeechAndCard
import com.marcelorcorrea.pregnancycalculator.util.Util.getLocaleFromRequest
import com.marcelorcorrea.pregnancycalculator.util.Util.getSlotValueFromRequest
import java.time.LocalDate
import java.time.Period
import java.util.Locale

class WeeksToMonthsIntent(private val pregnancyCalculatorService: PregnancyCalculatorService = PregnancyCalculatorService()) :
    IntentHandler {

    @Intent(mapsTo = ["WeeksToMonths"])
    override fun onIntentRequest(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val weeks = getSlotValueFromRequest(alexaRequest, WEEKS_SLOT)?.toLong()!! //auto delegation is on
        val period = pregnancyCalculatorService.calculatePeriodFrom(weeks)
        val months = period.months
        val days = period.days

        val pregnancyMessage = makeMonthsPregnancyMessage(months, days, i18nResource)
        return endInteractionWithSpeechAndCard(pregnancyMessage, getSkillName())
    }

    private fun makeMonthsPregnancyMessage(months: Int, days: Int, i18nResource: I18nResource): String {
        return if (months == 1 && days == 1) {
            i18nResource.getString("one_month_one_day")
        } else if (months == 1 && days > 1) {
            i18nResource.getString("one_month_days").format(days)
        } else if (months > 1 && days == 0) {
            i18nResource.getString("months").format(months)
        } else if (months > 1 && days == 1) {
            i18nResource.getString("months_one_day").format(months)
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

    companion object {
        const val WEEKS_SLOT = "weeks"
    }
}
