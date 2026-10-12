package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.services.ArticleRetrofitService
import javax.inject.Inject

class ArticleRetrofitDataSourceImplementation @Inject constructor(
    private val service: ArticleRetrofitService
) : ArticleRemoteDataSource {

    override suspend fun getAllArticles(): List<ArticleDto> = service.getAllArticles()

    override suspend fun getArticleById(id: String): ArticleDto = service.getArticleById(id)

    override suspend fun getArticleDetail(id: String): ArticleDto = service.getArticleDetail(id)

    override suspend fun getArticlesByUser(userId: String): List<ArticleDto> =
        service.getArticlesByUser(userId)

    override suspend fun createArticle(dto: CreateArticleDto): ArticleDto = service.createArticle(dto)

    override suspend fun updateArticle(id: String, dto: CreateArticleDto): ArticleDto =
        service.updateArticle(id, dto)

    override suspend fun deleteArticle(id: String) = service.deleteArticle(id)
}
