package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.intent.CancelIntentHandler
import com.hp.kalexa.core.intent.StopIntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.response.dsl.alexaResponse
import com.marcelorcorrea.pregnancycalculator.util.I18nResource

class CancelAndStopIntent : CancelIntentHandler, StopIntentHandler {

    override fun onCancelIntent(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        return goodbyeResponse(alexaRequest)
    }

    override fun onStopIntent(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        return goodbyeResponse(alexaRequest)
    }

    private fun goodbyeResponse(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val cancelAndStopMsg = i18nResource.getString("cancel_and_stop")
        return alexaResponse {
            response {
                speech {
                    cancelAndStopMsg
                }
                shouldEndSession = true
            }
        }
    }
}
