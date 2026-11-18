package com.example.kloth.data.datasource.services

/** Operaciones locales de identificación y traducción; no persiste contenido. */
interface TranslationRemoteDataSource {
    suspend fun identifyLanguage(text: String): String
    suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String): String
}
