package com.example.kloth.data.dataresource.services

import com.example.kloth.data.dto.ReviewDto
import retrofit2.http.GET

// Mismas rutas que review.routes.js del backend
interface ReviewRetrofitService {
    @GET("/reviews")
    suspend fun getReviews(): List<ReviewDto>
}
