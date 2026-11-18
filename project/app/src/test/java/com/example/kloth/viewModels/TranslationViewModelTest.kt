package com.example.kloth.viewModels

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.example.kloth.R
import com.example.kloth.data.datasource.services.TranslationRemoteDataSource
import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.ReviewData
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ReviewRepository
import com.example.kloth.data.repository.TranslationRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.screens.detail.DetailViewModel
import com.example.kloth.ui.screens.review.ReviewViewModel
import com.example.kloth.ui.utils.TranslationContentIds
import com.example.kloth.ui.utils.TranslationUiStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import androidx.lifecycle.SavedStateHandle
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class TranslationViewModelTest {
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
    fun detailViewModel_translatesDescriptionAndCanRestoreOriginal() = runTest(dispatcher) {
        val articleRepository = mockk<ArticleRepository>()
        val reviewRepository = mockk<ReviewRepository>(relaxed = true)
        val authRepository = mockk<AuthRepository>()
        every { authRepository.currentUser } returns null
        coEvery { articleRepository.getArticleDetail("article-1") } returns Result.success(
            ArticleInfo(
                id = "article-1",
                title = "Coat",
                brand = "Brand",
                price = "€10",
                categoryTag = "Prenda",
                colorName = "Black",
                categoryName = "Chaquetas y abrigos",
                description = "A warm coat"
            )
        )
        every { reviewRepository.observeReviewsForArticle("article-1") } returns flowOf(emptyList())
        val repository = TranslationRepository(FakeTranslationDataSource())
        val viewModel = DetailViewModel(
            articleRepository,
            reviewRepository,
            authRepository,
            repository,
            contextFor("es")
        )

        viewModel.loadProduct("article-1")
        advanceUntilIdle()
        val key = TranslationContentIds.description("article-1")
        assertThat(viewModel.uiState.value.translationStates[key]?.canTranslate).isTrue()

        viewModel.onTranslate(key, "A warm coat")
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.translationStates[key]?.status)
            .isEqualTo(TranslationUiStatus.Translated("es:A warm coat"))

        viewModel.onShowOriginal(key)
        assertThat(viewModel.uiState.value.translationStates[key]?.status)
            .isEqualTo(TranslationUiStatus.Original)
    }

    @Test
    fun reviewViewModel_translatesReviewTextAndExposesButtonOnlyForDifferentLanguage() = runTest(dispatcher) {
        val reviewRepository = mockk<ReviewRepository>(relaxed = true)
        val authRepository = mockk<AuthRepository>()
        every { authRepository.currentUser } returns null
        coEvery { reviewRepository.getReviewById("review-1", null) } returns Result.success(
            ReviewData(
                id = "review-1",
                authorName = "Alex",
                avatarRes = R.drawable.profile,
                rating = 5f,
                reviewText = "The fabric feels soft"
            )
        )
        val viewModel = ReviewViewModel(
            contextFor("es"),
            SavedStateHandle(mapOf(AppRoutes.Review.ARG_REVIEW_ID to "review-1")),
            reviewRepository,
            authRepository,
            mockk<UserRepository>(relaxed = true),
            TranslationRepository(FakeTranslationDataSource())
        )
        advanceUntilIdle()

        val key = TranslationContentIds.review("review-1")
        assertThat(viewModel.uiState.value.translationStates[key]?.canTranslate).isTrue()
        viewModel.onTranslate(key, "The fabric feels soft")
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.translationStates[key]?.status)
            .isEqualTo(TranslationUiStatus.Translated("es:The fabric feels soft"))
    }

    private fun contextFor(language: String): Context {
        val context = mockk<Context>()
        val resources = mockk<Resources>()
        val configuration = Configuration().apply { setLocale(Locale.forLanguageTag(language)) }
        every { context.resources } returns resources
        every { resources.configuration } returns configuration
        return context
    }

    private class FakeTranslationDataSource : TranslationRemoteDataSource {
        override suspend fun identifyLanguage(text: String) = "en"
        override suspend fun translate(text: String, sourceLanguage: String, targetLanguage: String) =
            "$targetLanguage:$text"
    }
}
