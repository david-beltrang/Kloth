package com.example.kloth.data.dto

import com.example.kloth.R
import com.example.kloth.data.local.ComentarioReviewData

data class CreateCommentDto(
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val text: String = "",
    val createdAt: String = "",
    val likesCount: Int = 0
)

data class CommentFirestoreDto(
    val commentId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val text: String = "",
    val createdAt: String = "",
    val likesCount: Int = 0
) {
    fun toComentarioReviewData(currentUserId: String?): ComentarioReviewData = ComentarioReviewData(
        id = commentId,
        authorId = userId,
        authorName = username,
        timeAgo = timeAgo(createdAt),
        avatarRes = R.drawable.profile,
        avatarUrl = profileImageUrl,
        commentText = text,
        likesCount = likesCount,
        isMyComment = userId == currentUserId
    )
}
