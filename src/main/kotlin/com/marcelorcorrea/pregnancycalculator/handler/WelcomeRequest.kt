package com.marcelorcorrea.pregnancycalculator.handler

import com.hp.kalexa.core.intent.LaunchRequestHandler
import com.hp.kalexa.core.util.Util
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.LaunchRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.response.dsl.alexaResponse
import com.marcelorcorrea.pregnancycalculator.util.I18nResource

class WelcomeRequest : LaunchRequestHandler {
    override fun onLaunchIntent(alexaRequest: AlexaRequest<LaunchRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val speechText = i18nResource.getString("welcome_message").format(Util.getSkillName())
        return alexaResponse {
            response {
                speech {
                    speechText
                }
                shouldEndSession = false
            }
        }
    }
}
