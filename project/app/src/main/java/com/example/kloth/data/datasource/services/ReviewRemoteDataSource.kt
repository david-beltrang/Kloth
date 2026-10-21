package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewRetrofitDto
import com.example.kloth.data.dto.UpdateReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDtoGeneric>
    suspend fun getReviewById(id: String, currentUserId: String = ""): ReviewDtoGeneric
    suspend fun createReview(dto: CreateReviewDto)
    suspend fun updateReview(id: String, dto: UpdateReviewDto)
    suspend fun deleteReview(id: String)

    //nuevos
    suspend fun sendOrDeleteLike(reviewId: String, userId: String): Unit
}
