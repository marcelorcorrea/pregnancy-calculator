package com.marcelorcorrea.pregnancycalculator.interceptor

import com.hp.kalexa.core.interceptor.RequestInterceptor
import com.hp.kalexa.model.request.AlexaRequest
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import java.util.Locale

class LocaleInterceptor : RequestInterceptor {

    override fun intercept(alexaRequest: AlexaRequest<*>) {
        val locale = Locale.forLanguageTag(alexaRequest.request.locale)
        val i18nResource = I18nResource()
        i18nResource.load(locale)
        alexaRequest.requestAttributes["locale"] = locale
        alexaRequest.requestAttributes["i18n"] = i18nResource
    }
}
