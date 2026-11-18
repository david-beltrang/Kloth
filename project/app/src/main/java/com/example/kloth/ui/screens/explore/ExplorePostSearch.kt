package com.example.kloth.ui.screens.explore

import com.example.kloth.data.local.PostItem
import java.text.Normalizer
import java.util.Locale

/** Búsqueda local sobre el feed cargado; no escala a muchos datos y a futuro conviene buscar en servidor. */
object ExplorePostSearch {
    private val combiningMarks = Regex("\\p{M}+")

    fun normalizeText(text: String): String = Normalizer
        .normalize(text.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(combiningMarks, "")

    fun filterPosts(
        posts: List<PostItem>,
        query: String,
        categoryIndex: Int,
        limit: Int = 50
    ): List<PostItem> {
        val normalizedQuery = normalizeText(query)
        val category = when (categoryIndex) {
            1 -> "prenda"
            2 -> "outfit"
            3 -> "marca"
            4 -> "evento"
            0 -> null
            else -> return emptyList()
        }

        return posts.asSequence()
            .filter { post ->
                val product = post.product
                val matchesCategory = category == null ||
                    normalizeText(product.categoryName).contains(category) ||
                    normalizeText(product.categoryTag).contains(category)
                val matchesQuery = normalizedQuery.isBlank() ||
                    normalizeText(product.title).contains(normalizedQuery) ||
                    normalizeText(product.description).contains(normalizedQuery)
                matchesCategory && matchesQuery
            }
            .sortedByDescending { it.product.createdAt.orEmpty() }
            .take(limit.coerceAtLeast(0))
            .toList()
    }
}
