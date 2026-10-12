package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.CreateArticleDto

interface ArticleRemoteDataSource {
    suspend fun getAllArticles(): List<ArticleDto>
    suspend fun getArticleById(id: String): ArticleDto
    suspend fun getArticleDetail(id: String): ArticleDto
    suspend fun getArticlesByUser(userId: String): List<ArticleDto>
    suspend fun createArticle(dto: CreateArticleDto): ArticleDto
    suspend fun updateArticle(id: String, dto: CreateArticleDto): ArticleDto
    suspend fun deleteArticle(id: String)
}
