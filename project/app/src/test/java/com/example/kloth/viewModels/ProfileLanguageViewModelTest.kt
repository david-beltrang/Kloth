package com.example.kloth.viewModels

import com.example.kloth.data.repository.AppLocaleRepository
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.screens.profile.ProfileViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileLanguageViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun profileViewModel_exposesCurrentLanguageAndAppliesSelection() = runTest(dispatcher) {
        val authRepository = mockk<AuthRepository>()
        val currentUser = mockk<FirebaseUser>()
        every { currentUser.uid } returns "user-1"
        every { currentUser.email } returns "user@example.com"
        every { currentUser.photoUrl } returns null
        every { authRepository.currentUser } returns currentUser

        val localeRepository = mockk<AppLocaleRepository>()
        every { localeRepository.currentLanguageTag() } returns "es"
        every { localeRepository.setApplicationLanguage("fr") } returns Unit
        val userRepository = mockk<UserRepository>()
        coEvery { userRepository.updateLanguageTag("user-1", "fr") } returns Result.success(Unit)

        val viewModel = ProfileViewModel(
            authRepository = authRepository,
            storageRepository = mockk<StorageRepository>(relaxed = true),
            userRepository = userRepository,
            articleRepository = mockk<ArticleRepository>(relaxed = true),
            appLocaleRepository = localeRepository
        )

        assertThat(viewModel.uiState.value.currentLanguageTag).isEqualTo("es")
        viewModel.onLanguageSelected("fr")
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.currentLanguageTag).isEqualTo("fr")
        verify(exactly = 1) { localeRepository.setApplicationLanguage("fr") }
        io.mockk.coVerify(exactly = 1) { userRepository.updateLanguageTag("user-1", "fr") }
    }
}
