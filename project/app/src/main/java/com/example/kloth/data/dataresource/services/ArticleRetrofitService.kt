package com.example.kloth.data.dataresource.services

import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.ReviewDto
import retrofit2.http.GET
import retrofit2.http.Path

// Mismas rutas que article.routes.js del backend
interface ArticleRetrofitService {
    @GET("/articles")
    suspend fun getArticles(): List<ArticleDto>

    @GET("/articles/{id}")
    suspend fun getArticleById(@Path("id") id: String): ArticleDto

    @GET("/articles/{id}/reviews")
    suspend fun getArticleReviews(@Path("id") id: String): List<ReviewDto>
}
