package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.annotation.Intent
import com.hp.kalexa.core.intent.IntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.marcelorcorrea.pregnancycalculator.model.GestationalAge
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import com.marcelorcorrea.pregnancycalculator.util.Util.endInteractionWithSpeech
import com.marcelorcorrea.pregnancycalculator.util.Util.endInteractionWithSpeechAndCard
import com.marcelorcorrea.pregnancycalculator.util.Util.getLocaleFromRequest
import com.marcelorcorrea.pregnancycalculator.util.Util.getSlotValueFromRequest
import com.marcelorcorrea.pregnancycalculator.util.Util.toLocalizedString
import java.time.LocalDate
import java.util.Locale

class ConceptionDateIntent(private val pregnancyCalculatorService: PregnancyCalculatorService = PregnancyCalculatorService()) :
    IntentHandler {

    @Intent(mapsTo = ["ConceptionIntent"])
    override fun onIntentRequest(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val locale: Locale = getLocaleFromRequest(alexaRequest)
        val dueDateString: String = getSlotValueFromRequest(alexaRequest, DUE_DATE_SLOT)!! //auto delegation is on
        val dueDate = LocalDate.parse(dueDateString)

        if (pregnancyCalculatorService.isDueDateExceedsLimit(dueDate)) {
            return endInteractionWithSpeech(i18nResource, "date_limit_exceeded")
        }
        val dateOfConception = pregnancyCalculatorService.calculateDateOfConception(dueDate)
        val lastMenstrualPeriod = pregnancyCalculatorService.calculateDateOfLastMenstrualPeriod(dateOfConception)
        val gestationalAge = pregnancyCalculatorService.calculateGestationalAge(lastMenstrualPeriod)

        val message = i18nResource.getString("date_of_conception_msg")
            .format(
                lastMenstrualPeriod.toLocalizedString(locale),
                dateOfConception.toLocalizedString(locale),
                makePregnancyMessage(gestationalAge, i18nResource)
            )

        return endInteractionWithSpeechAndCard(message, getSkillName())
    }

    private fun makePregnancyMessage(gestationalAge: GestationalAge, i18nResource: I18nResource): String {
        val weeks = gestationalAge.weeks
        val days = gestationalAge.days
        return if (weeks == 1 && days == 1) {
            i18nResource.getString("one_week_one_day")
        } else if (weeks == 1 && days > 1) {
            i18nResource.getString("one_week_days").format(days)
        } else if (weeks > 1 && days == 0) {
            i18nResource.getString("weeks").format(weeks)
        } else if (weeks > 1 && days == 1) {
            i18nResource.getString("weeks_one_day").format(weeks)
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

    companion object {
        private const val DUE_DATE_SLOT = "dueDate"
    }
}
