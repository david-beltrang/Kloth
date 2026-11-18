package com.example.kloth.data.datasource.implementation.mlkit

import com.example.kloth.data.datasource.services.TranslationRemoteDataSource
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationMlKitDataSource @Inject constructor() : TranslationRemoteDataSource {
    override suspend fun identifyLanguage(text: String): String {
        val identifier = LanguageIdentification.getClient()
        return try {
            identifier.identifyLanguage(text).await()
        } finally {
            identifier.close()
        }
    }

    override suspend fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        val source = TranslateLanguage.fromLanguageTag(sourceLanguage)
            ?: throw IllegalArgumentException("Idioma de origen no compatible")
        val target = TranslateLanguage.fromLanguageTag(targetLanguage)
            ?: throw IllegalArgumentException("Idioma de destino no compatible")
        val translator = Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(source)
                .setTargetLanguage(target)
                .build()
        )
        return try {
            // La descarga del modelo solo comienza cuando el usuario solicita traducir.
            translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            translator.translate(text).await()
        } finally {
            translator.close()
        }
    }
}
