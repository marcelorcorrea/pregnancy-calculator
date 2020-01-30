package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.intent.IntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import com.marcelorcorrea.pregnancycalculator.util.Util.endInteractionWithSpeech
import com.marcelorcorrea.pregnancycalculator.util.Util.endInteractionWithSpeechAndCard
import com.marcelorcorrea.pregnancycalculator.util.Util.getLocaleFromRequest
import com.marcelorcorrea.pregnancycalculator.util.Util.getSlotValueFromRequest
import com.marcelorcorrea.pregnancycalculator.util.Util.toLocalizedString
import java.time.LocalDate
import java.util.Locale

class DueDateIntent(private val pregnancyCalculatorService: PregnancyCalculatorService = PregnancyCalculatorService()) :
    IntentHandler {

    override fun onIntentRequest(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val locale: Locale = getLocaleFromRequest(alexaRequest)
        val lastMenstrualPeriodString: String =
            getSlotValueFromRequest(alexaRequest, LAST_MENSTRUAL_PERIOD)!! //auto delegation is on
        val lastMenstrualPeriod = LocalDate.parse(lastMenstrualPeriodString)

        if (pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(lastMenstrualPeriod)) {
            return endInteractionWithSpeech(i18nResource, "date_limit_exceeded")
        }
        val dueDate = pregnancyCalculatorService.calculateDueDate(lastMenstrualPeriod)
        val message =
            i18nResource.getString("due_date_msg").format(dueDate.toLocalizedString(locale))
        return endInteractionWithSpeechAndCard(message, getSkillName())
    }

    companion object {
        private const val LAST_MENSTRUAL_PERIOD = "lastMenstrualPeriod"
    }
}
