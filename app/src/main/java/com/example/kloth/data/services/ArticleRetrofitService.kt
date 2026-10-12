package com.example.kloth.data.services

import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.CreateArticleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ArticleRetrofitService {

    @GET("articles")
    suspend fun getAllArticles(): List<ArticleDto>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") id: String): ArticleDto

    @GET("articles/{id}/detail")
    suspend fun getArticleDetail(@Path("id") id: String): ArticleDto

    @GET("users/{id}/articles")
    suspend fun getArticlesByUser(@Path("id") userId: String): List<ArticleDto>

    @POST("articles")
    suspend fun createArticle(@Body dto: CreateArticleDto): ArticleDto

    @PUT("articles/{id}")
    suspend fun updateArticle(@Path("id") id: String, @Body dto: CreateArticleDto): ArticleDto

    @DELETE("articles/{id}")
    suspend fun deleteArticle(@Path("id") id: String)
}
