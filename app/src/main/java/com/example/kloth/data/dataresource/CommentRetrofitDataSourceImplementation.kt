package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.CommentDto
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.UpdateCommentDto
import com.example.kloth.data.services.CommentRetrofitService
import javax.inject.Inject

class CommentRetrofitDataSourceImplementation @Inject constructor(
    private val service: CommentRetrofitService
) : CommentRemoteDataSource {

    override suspend fun getCommentsByReview(reviewId: String): List<CommentDto> =
        service.getCommentsByReview(reviewId)

    override suspend fun createComment(reviewId: String, dto: CreateCommentDto): CommentDto =
        service.createComment(reviewId, dto)

    override suspend fun updateComment(id: String, dto: UpdateCommentDto): CommentDto =
        service.updateComment(id, dto)

    override suspend fun deleteComment(id: String) = service.deleteComment(id)
}
