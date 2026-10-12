package com.example.kloth.data.services

import com.example.kloth.data.dto.CommentDto
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.UpdateCommentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommentRetrofitService {

    @GET("reviews/{id}/comments")
    suspend fun getCommentsByReview(@Path("id") reviewId: String): List<CommentDto>

    @POST("reviews/{id}/comments")
    suspend fun createComment(
        @Path("id") reviewId: String,
        @Body dto: CreateCommentDto
    ): CommentDto

    @PUT("comments/{id}")
    suspend fun updateComment(
        @Path("id") id: String,
        @Body dto: UpdateCommentDto
    ): CommentDto

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") id: String)
}
