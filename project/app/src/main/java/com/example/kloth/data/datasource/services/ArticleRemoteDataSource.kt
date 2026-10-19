package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.UpdateArticleDto

// Contrato: sin importar la tecnologia (Retrofit, SQLite, Firestore) estas funciones devuelven lo mismo
interface ArticleRemoteDataSource {
    suspend fun getAllArticles(): List<ArticleDtoGeneric>
    suspend fun getArticleById(id: String): ArticleDtoGeneric
    suspend fun getArticleReviews(id: String): List<ReviewDtoGeneric>
    suspend fun createArticle(article: CreateArticleDto)
    suspend fun updateArticle(id: String, article: UpdateArticleDto)
    suspend fun deleteArticle(id: String)
}
