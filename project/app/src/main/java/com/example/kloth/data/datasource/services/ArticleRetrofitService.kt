package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.ArticleRetrofitDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.ReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Mismas rutas que article.routes.js del backend
interface ArticleRetrofitService {
    @GET("/articles")
    suspend fun getArticles(): List<ArticleDtoGeneric>

    @GET("/articles/{id}")
    suspend fun getArticleById(@Path("id") id: String): ArticleDtoGeneric

    @GET("/articles/{id}/reviews")
    suspend fun getArticleReviews(@Path("id") id: String): List<ReviewDto>

    @POST("/articles")
    suspend fun createArticle(@Body dto: CreateArticleDto)

    @POST("/articles/{id}")
    suspend fun updateArticle(@Path("id") id: String, @Body dto: CreateArticleDto)

    @DELETE("/articles/{id}")
    suspend fun deleteArticle(@Path("id") id: String)
}
