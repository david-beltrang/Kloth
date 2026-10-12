package com.example.kloth.data.services

import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDto
import com.example.kloth.data.dto.UpdateReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewRetrofitService {

    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDto>

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewDto

    @GET("articles/{id}/reviews")
    suspend fun getReviewsByArticle(@Path("id") articleId: String): List<ReviewDto>

    @GET("users/{id}/reviews")
    suspend fun getReviewsByUser(@Path("id") userId: String): List<ReviewDto>

    @POST("reviews")
    suspend fun createReview(@Body dto: CreateReviewDto): ReviewDto

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body dto: UpdateReviewDto): ReviewDto

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String)
}
