package com.example.kloth.repository

import com.example.kloth.data.repository.resolveApplicationLanguage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppLocaleRepositoryTest {
    @Test
    fun resolveApplicationLanguage_prefersTheSelectedLanguage() {
        assertThat(resolveApplicationLanguage("fr-CA", "en-US")).isEqualTo("fr")
    }

    @Test
    fun resolveApplicationLanguage_usesSupportedSystemLanguageWhenNoneWasSelected() {
        assertThat(resolveApplicationLanguage(null, "pt-BR")).isEqualTo("pt")
    }

    @Test
    fun resolveApplicationLanguage_fallsBackToSpanishForUnsupportedLanguages() {
        assertThat(resolveApplicationLanguage(null, "ja-JP")).isEqualTo("es")
    }
}
