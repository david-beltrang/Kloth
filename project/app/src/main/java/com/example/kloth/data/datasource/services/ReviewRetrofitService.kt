package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewRetrofitDto
import com.example.kloth.data.dto.UpdateReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Mismas rutas que review.routes.js del backend
interface ReviewRetrofitService {
    @GET("/reviews")
    suspend fun getReviews(): List<ReviewRetrofitDto>

    @GET("/reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewRetrofitDto

    @POST("/reviews")
    suspend fun createReview(@Body dto: CreateReviewDto)

    @PUT("/reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body dto: UpdateReviewDto)

    @DELETE("/reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String)
}
