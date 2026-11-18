package com.example.kloth.data.repository

import android.content.Context
import com.example.kloth.data.datasource.services.AppLocaleDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleRepository @Inject constructor(
    private val dataSource: AppLocaleDataSource,
    @ApplicationContext private val context: Context
) {
    fun currentLanguageTag(): String {
        val configuration = context.resources.configuration
        val systemLanguageTag = (configuration.locales?.get(0) ?: configuration.locale)?.toLanguageTag()
        return resolveApplicationLanguage(dataSource.selectedLanguageTag(), systemLanguageTag)
    }

    fun applyProcessLocale() {
        Locale.setDefault(Locale.forLanguageTag(currentLanguageTag()))
    }

    fun setApplicationLanguage(languageTag: String) {
        val normalizedLanguage = resolveApplicationLanguage(languageTag, null)
        dataSource.setApplicationLanguage(normalizedLanguage)
        Locale.setDefault(Locale.forLanguageTag(normalizedLanguage))
    }

}

fun resolveApplicationLanguage(selectedLanguageTag: String?, systemLanguageTag: String?): String {
    fun supportedLanguage(tag: String?): String? {
        val language = tag?.let { Locale.forLanguageTag(it).language }?.lowercase(Locale.ROOT)
        return language?.takeIf { it in SUPPORTED_APP_LANGUAGES }
    }

    return supportedLanguage(selectedLanguageTag)
        ?: supportedLanguage(systemLanguageTag)
        ?: DEFAULT_APP_LANGUAGE
}

private val SUPPORTED_APP_LANGUAGES = setOf("es", "en", "pt", "fr", "de", "it", "ru", "ar")
private const val DEFAULT_APP_LANGUAGE = "es"
