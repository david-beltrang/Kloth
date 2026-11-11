package com.example.kloth.viewModels

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.ui.screens.explore.ExploreViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {
    private val dispatcher = kotlinx.coroutines.test.StandardTestDispatcher()
    private val articleRepository = mockk<ArticleRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun queryDebouncesCancelsPreviousAndCategoryKeepsCurrentQuery() = runTest(dispatcher) {
        val posts = listOf(
            makePost("jacket", "Chaqueta", "Abrigo", "Prenda", "2026-01-03"),
            makePost("shirt", "Camiseta", "Chaqueta estampada", "Prenda", "2026-01-02"),
            makePost("event", "Chaqueta evento", "", "Evento", "2026-01-01")
        )
        coEvery { articleRepository.getFeedPosts() } returns Result.success(posts)
        val viewModel = ExploreViewModel(articleRepository)

        viewModel.getAllPosts()
        advanceUntilIdle()
        viewModel.onQueryChange("camiseta")
        advanceTimeBy(299)
        assertThat(viewModel.uiState.value.filteredPosts).hasSize(3)

        viewModel.onQueryChange("chaqueta")
        advanceTimeBy(299)
        assertThat(viewModel.uiState.value.filteredPosts).hasSize(3)
        advanceTimeBy(1)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.filteredPosts.map { it.id })
            .containsExactly("jacket", "shirt", "event").inOrder()
        viewModel.onCategorySelected(1)
        assertThat(viewModel.uiState.value.filteredPosts.map { it.id })
            .containsExactly("jacket", "shirt").inOrder()
    }

    private fun makePost(id: String, title: String, description: String, category: String, date: String) = PostItem(
        id = id,
        creatorName = "",
        timeAgo = "",
        avatarUrl = "",
        commentsCount = "0",
        tags = emptyList(),
        product = ArticleInfo(
            id = id,
            title = title,
            brand = "",
            price = "",
            categoryTag = category,
            colorName = "",
            categoryName = category,
            description = description,
            createdAt = date
        )
    )
}
