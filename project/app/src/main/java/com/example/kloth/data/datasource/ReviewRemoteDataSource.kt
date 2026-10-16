package com.example.kloth.data.datasource

import com.example.kloth.data.dto.ReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
}
