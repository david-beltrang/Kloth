package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
}
