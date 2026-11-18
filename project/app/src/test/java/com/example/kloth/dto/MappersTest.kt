package com.example.kloth.dto

import android.text.format.DateUtils
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.dto.formatPrice
import java.util.Locale
import com.example.kloth.data.dto.timeAgo
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

// Pruebas unitarias de mapeos / transformaciones (sin Firebase).
class MappersTest {

    @Test
    fun toUserProfileInfo_mapsDtoToUi() {
        // arrange
        val dto = UserFirestoreDto(
            userId = "abc",
            username = "kloth_user",
            email = "u@test.com",
            fullName = "Kloth User",
            biography = "Hola",
            location = "Bogotá",
            website = "https://kloth.app",
            imageURL = "https://img.test/a.jpg",
            followersCount = 120,
            followingCount = 40
        )

        // act
        val ui = dto.toUserProfileInfo()

        // assert
        Truth.assertThat(ui.id).isEqualTo("abc")
        Truth.assertThat(ui.username).isEqualTo("kloth_user")
        Truth.assertThat(ui.email).isEqualTo("u@test.com")
        Truth.assertThat(ui.bio).isEqualTo("Hola")
        Truth.assertThat(ui.location).isEqualTo("Bogotá")
        Truth.assertThat(ui.website).isEqualTo("https://kloth.app")
        Truth.assertThat(ui.profileImageUrl).isEqualTo("https://img.test/a.jpg")
        Truth.assertThat(ui.followersCount).isEqualTo(120)
        Truth.assertThat(ui.followingCount).isEqualTo(40)
    }

    @Test
    fun toUserProfileInfo_blankUsername_usesFullName() {
        // arrange
        val dto = UserFirestoreDto(
            userId = "x",
            username = "   ",
            email = "x@test.com",
            fullName = "Nombre Completo"
        )

        // act
        val ui = dto.toUserProfileInfo()

        // assert
        Truth.assertThat(ui.username).isEqualTo("Nombre Completo")
    }

    @Test
    fun formatPrice_formatsWithCurrencySymbol() {
        // En Kloth el formateo de presentación es el precio (en el video era 14.614.124 -> 14k)
        val formatted = formatPrice(129.9)

        Truth.assertThat(formatted).startsWith("$")
        Truth.assertThat(formatted).contains("129")
    }

    @Test
    fun formatPrice_usesLocaleSeparatorsWithoutChangingCurrencySymbol() {
        val formatted = formatPrice(1234.5, locale = Locale.GERMANY)

        Truth.assertThat(formatted).isEqualTo("$1.234,5")
    }

    @Test
    fun timeAgo_recentTimestamp_returnsMinutesLabel() {
        mockkStatic(DateUtils::class)
        try {
            every {
                DateUtils.getRelativeTimeSpanString(any<Long>(), any<Long>(), any<Long>())
            } returns "Hace 5 min"
            val createdAt = Instant.now().minus(5, ChronoUnit.MINUTES).toString()

            val label = timeAgo(createdAt)

            Truth.assertThat(label).isEqualTo("Hace 5 min")
        } finally {
            unmockkStatic(DateUtils::class)
        }
    }
}

