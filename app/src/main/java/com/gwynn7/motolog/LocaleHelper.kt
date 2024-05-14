package com.gwynn7.motolog

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

object LocaleHelper {
    private val languageKey = stringPreferencesKey("language")

    enum class Language(val value: String){
        ITA("it"),
        ENG("en")
    }
    fun onAttach(context: Context): Context {
        val lang = getLanguage(context)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): Language {
        return fromLanguage(getPersistedData(context, Language.ENG.value))
    }

    fun setLocale(context: Context, language: Language): Context {
        persist(context, language.value)
        return updateResources(context, language.value)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        return runBlocking {
            val settings = context.settings.data.first()
            settings[languageKey] ?: defaultLanguage
        }}

    private fun persist(context: Context, language: String) {
        runBlocking { context.settings.edit { settings -> settings[languageKey] = language }}
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun fromLanguage(value: String): Language = Language.entries.first { it.value == value }
}