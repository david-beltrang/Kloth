package com.example.kloth.data.datasource.implementation.retrofit

import com.example.kloth.data.datasource.services.ArticleRemoteDataSource
import com.example.kloth.data.datasource.services.ArticleRetrofitService
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.ReviewDto
import com.example.kloth.data.dto.UpdateArticleDto
import javax.inject.Inject

class ArticleRetrofitDataSourceImpl @Inject constructor(
    private val service: ArticleRetrofitService
) : ArticleRemoteDataSource {
    override suspend fun getAllArticles(): List<ArticleDtoGeneric> = service.getArticles()
    override suspend fun getArticleById(id: String): ArticleDtoGeneric = service.getArticleById(id)
    override suspend fun getArticleReviews(id: String): List<ReviewDto> =
        service.getArticleReviews(id)

    override suspend fun createArticle(article: CreateArticleDto) = service.createArticle(article)

    override suspend fun updateArticle(id: String, article: UpdateArticleDto) {
        throw UnsupportedOperationException("Los artículos se editan en Firestore")
    }

    override suspend fun deleteArticle(id: String) {
        TODO("Not yet implemented")
    }
}