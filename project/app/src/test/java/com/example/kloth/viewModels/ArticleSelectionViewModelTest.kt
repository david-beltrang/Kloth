package com.example.kloth.viewModels

import androidx.lifecycle.SavedStateHandle
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.UpdateArticleDto
import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.screens.createArticle.CreateArticleViewModel
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import com.example.kloth.ui.screens.editArticle.EditArticleViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
class ArticleSelectionViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val articleRepository = mockk<ArticleRepository>(relaxed = true)
    private val authRepository = mockk<AuthRepository>(relaxed = true)
    private val storageRepository = mockk<StorageRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val user = mockk<FirebaseUser>()
        every { user.uid } returns "user-1"
        every { authRepository.currentUser } returns user
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun create_savesCategoryOnlyForPrenda_andStyleRemainsOptional() = runTest(dispatcher) {
        val createdArticles = mutableListOf<CreateArticleDto>()
        coEvery { articleRepository.createArticle(capture(createdArticles)) } returns Result.success(Unit)
        val viewModel = CreateArticleViewModel(articleRepository, authRepository, storageRepository)
        viewModel.onNombreChanged("Artículo")
        viewModel.onCategoriaChanged("Chaquetas y abrigos")
        viewModel.publishArticle {}
        advanceUntilIdle()

        viewModel.onTypeSelected(ArticleType.OUTFIT)
        viewModel.onEstiloChanged("")
        viewModel.publishArticle {}
        advanceUntilIdle()

        assertThat(createdArticles).hasSize(2)
        assertThat(createdArticles[0].category).isEqualTo("Chaquetas y abrigos")
        assertThat(createdArticles[1].category).isNull()
        assertThat(createdArticles[1].style).isNull()
    }

    @Test
    fun create_requiresCategoryForPrenda() = runTest(dispatcher) {
        val viewModel = CreateArticleViewModel(articleRepository, authRepository, storageRepository)
        viewModel.onNombreChanged("Artículo")
        viewModel.publishArticle {}
        advanceUntilIdle()

        coVerify(exactly = 0) { articleRepository.createArticle(any()) }
    }

    @Test
    fun edit_requiresCategoryForPrenda() = runTest(dispatcher) {
        coEvery { articleRepository.getArticleForEdit("article-1") } returns Result.success(article(category = ""))
        val viewModel = editViewModel()
        advanceUntilIdle()
        viewModel.saveChanges {}
        advanceUntilIdle()

        coVerify(exactly = 0) { articleRepository.updateArticle(any(), any(), any()) }
    }

    @Test
    fun edit_savesCategoryForPrenda() = runTest(dispatcher) {
        coEvery { articleRepository.getArticleForEdit("article-1") } returns
            Result.success(article(category = "Chaquetas y abrigos"))
        val updateSlot = io.mockk.slot<UpdateArticleDto>()
        coEvery { articleRepository.updateArticle("article-1", capture(updateSlot), null) } returns Result.success(Unit)
        val viewModel = editViewModel()
        advanceUntilIdle()
        viewModel.saveChanges {}
        advanceUntilIdle()

        assertThat(updateSlot.captured.category).isEqualTo("Chaquetas y abrigos")
    }

    @Test
    fun edit_keepsLegacyStyleAndLoadsExistingCategory() = runTest(dispatcher) {
        coEvery { articleRepository.getArticleForEdit("article-1") } returns Result.success(
            article(type = "Outfit", category = "", style = "Estilo creado anteriormente")
        )
        val updateSlot = io.mockk.slot<UpdateArticleDto>()
        coEvery { articleRepository.updateArticle("article-1", capture(updateSlot), null) } returns Result.success(Unit)
        val viewModel = editViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.estilo).isEqualTo("Estilo creado anteriormente")
        viewModel.saveChanges {}
        advanceUntilIdle()

        assertThat(updateSlot.captured.style).isEqualTo("Estilo creado anteriormente")

        coEvery { articleRepository.getArticleForEdit("article-1") } returns Result.success(article(category = "Chaquetas y abrigos"))
        val prendaViewModel = editViewModel()
        advanceUntilIdle()
        assertThat(prendaViewModel.uiState.value.categoria).isEqualTo("Chaquetas y abrigos")
    }

    private fun editViewModel() = EditArticleViewModel(
        savedStateHandle = SavedStateHandle(mapOf(AppRoutes.EditArticle.ARG_ARTICLE_ID to "article-1")),
        articleRepository = articleRepository,
        storageRepository = storageRepository,
        authRepository = authRepository
    )

    private fun article(
        type: String = "Prenda",
        category: String,
        style: String? = null
    ) = ArticleInfo(
        id = "article-1",
        title = "Artículo",
        brand = "",
        price = "",
        categoryTag = type,
        colorName = "",
        categoryName = category,
        description = "Descripción",
        style = style
    )
}
