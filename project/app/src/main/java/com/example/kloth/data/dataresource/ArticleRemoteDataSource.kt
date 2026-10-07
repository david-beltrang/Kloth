package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.ReviewDto

// Contrato: sin importar la tecnologia (Retrofit, SQLite, Firestore) estas funciones devuelven lo mismo
interface ArticleRemoteDataSource {
    suspend fun getAllArticles(): List<ArticleDto>
    suspend fun getArticleById(id: String): ArticleDto
    suspend fun getArticleReviews(id: String): List<ReviewDto>
}
