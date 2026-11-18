package com.example.kloth.data.datasource.implementation.platform

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.kloth.data.datasource.services.AppLocaleDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleDataSourceImpl @Inject constructor() : AppLocaleDataSource {
    override fun selectedLanguageTag(): String? =
        AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag()

    override fun setApplicationLanguage(languageTag: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }
}
