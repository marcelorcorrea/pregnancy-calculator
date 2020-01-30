package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.intent.FallbackIntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.response.dsl.alexaResponse
import com.marcelorcorrea.pregnancycalculator.util.I18nResource

class FallbackIntent : FallbackIntentHandler {

    override fun onFallbackIntent(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val fallbackMessage = i18nResource.getString("fallback_message")
        return alexaResponse {
            response {
                speech {
                    fallbackMessage
                }
            }
        }
    }
}
