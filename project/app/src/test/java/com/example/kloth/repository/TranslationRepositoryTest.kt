package com.example.kloth.repository

import com.example.kloth.data.datasource.services.TranslationRemoteDataSource
import com.example.kloth.data.repository.TranslationOutcome
import com.example.kloth.data.repository.TranslationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TranslationRepositoryTest {
    @Test
    fun translate_usesRequestedTargetAndCachesByContentAndTarget() = runTest {
        val source = FakeTranslationDataSource(sourceLanguage = "en")
        val repository = TranslationRepository(source)

        assertEquals(
            TranslationOutcome.Translated("es:Hola"),
            repository.translate("review-1", "Hello", "es")
        )
        assertEquals(
            TranslationOutcome.Translated("es:Hola"),
            repository.translate("review-1", "Hello", "es")
        )

        assertEquals(listOf("en->es"), source.translationCalls)
    }

    @Test
    fun translate_keepsSeparateCacheForEachTargetLanguage() = runTest {
        val source = FakeTranslationDataSource(sourceLanguage = "es")
        val repository = TranslationRepository(source)

        repository.translate("comment-1", "Hola", "en")
        repository.translate("comment-1", "Hola", "es")

        assertEquals(listOf("es->en"), source.translationCalls)
        assertEquals(TranslationOutcome.SameLanguage("es"), repository.translate("comment-1", "Hola", "es"))
    }

    @Test
    fun translate_usesUpdatedTextInsteadOfCachedTranslationForSameContent() = runTest {
        val source = FakeTranslationDataSource(sourceLanguage = "en")
        val repository = TranslationRepository(source)

        assertEquals(
            TranslationOutcome.Translated("es:First version"),
            repository.translate("review-1", "First version", "es")
        )
        assertEquals(
            TranslationOutcome.Translated("es:Edited version"),
            repository.translate("review-1", "Edited version", "es")
        )

        assertEquals(listOf("First version", "Edited version"), source.translatedTexts)
    }

    @Test
    fun canTranslate_isFalseWhenTextAlreadyMatchesTargetOrIsUndetermined() = runTest {
        val englishSource = FakeTranslationDataSource(sourceLanguage = "en")
        val englishRepository = TranslationRepository(englishSource)
        assertFalse(englishRepository.canTranslate("same", "Hello", "en"))
        assertTrue(englishRepository.canTranslate("same", "Hello", "es"))

        val unknownRepository = TranslationRepository(FakeTranslationDataSource(sourceLanguage = "und"))
        assertFalse(unknownRepository.canTranslate("unknown", "12345", "es"))
        assertEquals(TranslationOutcome.Undetermined, unknownRepository.translate("unknown", "12345", "es"))
    }

    private class FakeTranslationDataSource(
        private val sourceLanguage: String
    ) : TranslationRemoteDataSource {
        val translationCalls = mutableListOf<String>()
        val translatedTexts = mutableListOf<String>()

        override suspend fun identifyLanguage(text: String): String = sourceLanguage

        override suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String): String {
            translationCalls += "$sourceLanguage->$targetLanguage"
            translatedTexts += text
            return if (text == "Hello") "$targetLanguage:Hola" else "$targetLanguage:$text"
        }
    }
}
