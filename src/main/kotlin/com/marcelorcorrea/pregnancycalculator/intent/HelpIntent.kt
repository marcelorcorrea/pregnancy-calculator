package com.marcelorcorrea.pregnancycalculator.intent

import com.hp.kalexa.core.intent.HelpIntentHandler
import com.hp.kalexa.model.extension.getVal
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.hp.kalexa.model.response.AlexaResponse
import com.hp.kalexa.model.response.dsl.alexaResponse
import com.marcelorcorrea.pregnancycalculator.util.I18nResource

class HelpIntent : HelpIntentHandler {

    override fun onHelpIntent(alexaRequest: AlexaRequest<IntentRequest>): AlexaResponse {
        val i18nResource = alexaRequest.requestAttributes.getVal<I18nResource>("i18n")
        val helpMessage = i18nResource.getString("skill_help")
        return alexaResponse {
            response {
                shouldEndSession = false
                speech {
                    helpMessage
                }
                simpleCard {
                    title = getSkillName()
                    content = helpMessage
                }
            }
        }
    }
}
