package com.example.kloth.data.datasource.services

interface AppLocaleDataSource {
    fun selectedLanguageTag(): String?
    fun setApplicationLanguage(languageTag: String)
}
