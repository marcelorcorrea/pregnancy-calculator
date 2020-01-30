package com.marcelorcorrea.pregnancycalculator.util

import com.hp.kalexa.core.extension.cast
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.response.dsl.alexaResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object Util {
    fun getLocaleFromRequest(alexaRequest: AlexaRequest<IntentRequest>): Locale =
        alexaRequest.requestAttributes.getValue("locale").cast()

    fun getSlotValueFromRequest(alexaRequest: AlexaRequest<IntentRequest>, slotName: String) =
        alexaRequest.request.intent.getSlot(slotName)?.value

    fun endInteractionWithSpeech(i18nResource: I18nResource, msgId: String): AlexaResponse {
        val message = i18nResource.getString(msgId)
        return endInteractionWithSpeech(message)
    }

    fun endInteractionWithSpeech(msg: String): AlexaResponse {
        return alexaResponse {
            response {
                speech { msg }
                shouldEndSession = true
            }
        }
    }

    fun LocalDate.toLocalizedString(locale: Locale): String {
        return format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(locale)
        )
    }

    fun endInteractionWithSpeechAndCard(msg: String, cardTitle: String): AlexaResponse {
        return alexaResponse {
            response {
                speech { msg }
                simpleCard {
                    title = cardTitle
                    content = msg
                }
                shouldEndSession = true
            }
        }
    }
}
