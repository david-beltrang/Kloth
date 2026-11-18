package com.example.kloth.data.repository

import com.example.kloth.data.datasource.services.TranslationRemoteDataSource
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

sealed interface TranslationOutcome {
    data class Translated(val text: String) : TranslationOutcome
    data class SameLanguage(val language: String) : TranslationOutcome
    data object Undetermined : TranslationOutcome
}

@Singleton
class TranslationRepository @Inject constructor(
    private val dataSource: TranslationRemoteDataSource
) {
    private val detectedLanguages = ConcurrentHashMap<DetectionKey, String>()
    private val translations = ConcurrentHashMap<TranslationKey, String>()

    suspend fun canTranslate(contentId: String, text: String, targetLanguage: String): Boolean {
        if (text.isBlank() || targetLanguage !in SUPPORTED_LANGUAGES) return false
        val sourceLanguage = detect(contentId, text)
        return sourceLanguage != UNDETERMINED && sourceLanguage != targetLanguage
    }

    suspend fun translate(
        contentId: String,
        text: String,
        targetLanguage: String
    ): TranslationOutcome {
        val key = TranslationKey(contentId, text, targetLanguage)
        translations[key]?.let { return TranslationOutcome.Translated(it) }
        val sourceLanguage = detect(contentId, text)
        if (sourceLanguage == UNDETERMINED) return TranslationOutcome.Undetermined
        if (sourceLanguage == targetLanguage) return TranslationOutcome.SameLanguage(sourceLanguage)
        if (targetLanguage !in SUPPORTED_LANGUAGES) return TranslationOutcome.Undetermined

        val translatedText = dataSource.translate(text, sourceLanguage, targetLanguage)
        translations[key] = translatedText
        return TranslationOutcome.Translated(translatedText)
    }

    private suspend fun detect(contentId: String, text: String): String {
        val key = DetectionKey(contentId, text)
        return detectedLanguages[key] ?: dataSource.identifyLanguage(text)
            .lowercase(Locale.ROOT)
            .also { detectedLanguages[key] = it }
    }

    private data class DetectionKey(val contentId: String, val text: String)
    private data class TranslationKey(val contentId: String, val text: String, val targetLanguage: String)

    private companion object {
        const val UNDETERMINED = "und"
        val SUPPORTED_LANGUAGES = setOf("es", "en")
    }
}
