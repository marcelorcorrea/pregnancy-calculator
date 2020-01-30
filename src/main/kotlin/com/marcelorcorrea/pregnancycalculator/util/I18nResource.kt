package com.marcelorcorrea.pregnancycalculator.util

import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle
import java.util.WeakHashMap

class I18nResource {
    companion object {
        private const val BASE_NAME = "locale/messages"
    }

    private val cache = WeakHashMap<Locale, ResourceBundle?>()
    private var resources: ResourceBundle? = null

    fun load(locale: Locale) {
        resources = cache[locale]
        if (resources == null) {
            resources = ResourceBundle.getBundle(
                BASE_NAME, locale,
                UTF8Control()
            )
            cache[locale] = resources
        }
    }

    fun getString(key: String): String {
        return try {
            resources!!.getString(key)
        } catch (mre: MissingResourceException) {
            key
        }
    }
}
