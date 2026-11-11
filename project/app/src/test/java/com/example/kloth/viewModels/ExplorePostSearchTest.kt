package com.example.kloth.viewModels

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import com.example.kloth.ui.screens.explore.ExplorePostSearch
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExplorePostSearchTest {
    private fun post(
        id: String,
        title: String,
        description: String,
        categoryName: String = "Prenda",
        categoryTag: String = "Prenda",
        createdAt: String
    ) = PostItem(
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
            categoryTag = categoryTag,
            colorName = "",
            categoryName = categoryName,
            description = description,
            createdAt = createdAt
        )
    )

    @Test
    fun normalizeText_trimsLowercasesAndRemovesAccents() {
        assertThat(ExplorePostSearch.normalizeText("  Chaquéta Ázul "))
            .isEqualTo("chaqueta azul")
    }

    @Test
    fun filterPosts_matchesTitleOrDescription() {
        val posts = listOf(
            post("title", "Chaqueta azul", "Abrigo para invierno", createdAt = "2026-01-03"),
            post("description", "Abrigo ligero", "Chaqueta azul para invierno", createdAt = "2026-01-02"),
            post("none", "Pantalón", "Ropa casual", createdAt = "2026-01-01")
        )

        val result = ExplorePostSearch.filterPosts(posts, "CHAQUÉTA", categoryIndex = 0)

        assertThat(result.map { it.id }).containsExactly("title", "description").inOrder()
    }

    @Test
    fun filterPosts_combinesQueryAndCategory_andLimitsAfterSorting() {
        val matches = (1..55).map { index ->
            post("p$index", "Chaqueta $index", "", createdAt = "2026-01-%02d".format(index))
        }
        val otherCategory = post(
            "outfit", "Chaqueta especial", "", categoryName = "Outfit", categoryTag = "Outfit",
            createdAt = "2027-01-01"
        )

        val result = ExplorePostSearch.filterPosts(matches + otherCategory, "chaqueta", categoryIndex = 1)

        assertThat(result).hasSize(50)
        assertThat(result.first().id).isEqualTo("p55")
        assertThat(result.none { it.id == "outfit" }).isTrue()
    }

    @Test
    fun filterPosts_emptyQueryReturnsLatest50ForActiveCategory() {
        val posts = (1..55).map { index ->
            post("p$index", "Prenda $index", "", createdAt = "2026-01-%02d".format(index))
        } + post(
            "evento", "Evento nuevo", "", categoryName = "Evento", categoryTag = "Evento",
            createdAt = "2027-01-01"
        )

        val result = ExplorePostSearch.filterPosts(posts, "  ", categoryIndex = 1)

        assertThat(result).hasSize(50)
        assertThat(result.first().id).isEqualTo("p55")
        assertThat(result.none { it.id == "evento" }).isTrue()
    }

    @Test
    fun filterPosts_existingTypeChipStillMatchesNewClothingCategories() {
        val clothingPost = post(
            "clothing",
            "Chaqueta de lana",
            "",
            categoryName = "Chaquetas y abrigos",
            categoryTag = "Prenda",
            createdAt = "2026-01-01"
        )

        val result = ExplorePostSearch.filterPosts(listOf(clothingPost), query = "", categoryIndex = 1)

        assertThat(result.map { it.id }).containsExactly("clothing")
    }
}
