package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.CommentDto
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.UpdateCommentDto

interface CommentRemoteDataSource {
    suspend fun getCommentsByReview(reviewId: String): List<CommentDto>
    suspend fun createComment(reviewId: String, dto: CreateCommentDto): CommentDto
    suspend fun updateComment(id: String, dto: UpdateCommentDto): CommentDto
    suspend fun deleteComment(id: String)
}
