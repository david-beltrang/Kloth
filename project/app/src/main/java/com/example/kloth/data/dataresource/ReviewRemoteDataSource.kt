package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.ReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
}
