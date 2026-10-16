package com.example.kloth.data.datasource.implementation

import com.example.kloth.data.datasource.ArticleRemoteDataSource
import com.example.kloth.data.datasource.services.ArticleRetrofitService
import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.ReviewDto
import javax.inject.Inject

class ArticleRetrofitDataSourceImpl @Inject constructor(
    private val service: ArticleRetrofitService
) : ArticleRemoteDataSource {
    override suspend fun getAllArticles(): List<ArticleDto> = service.getArticles()
    override suspend fun getArticleById(id: String): ArticleDto = service.getArticleById(id)
    override suspend fun getArticleReviews(id: String): List<ReviewDto> = service.getArticleReviews(id)
}
