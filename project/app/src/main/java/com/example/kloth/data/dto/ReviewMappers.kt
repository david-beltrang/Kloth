package com.example.kloth.data.dto

import com.example.kloth.data.Session
import com.example.kloth.data.local.ComentarioReviewData
import com.example.kloth.data.local.ReviewData

/**
 * Reseña -> modelo visual. El autor se resuelve con `GET /users` en el
 * repositorio; `comments` es la lista ya mapeada de esa reseña.
 */
fun ReviewDto.toReviewData(
    author: UserDto? = null,
    comments: List<ComentarioReviewData> = emptyList(),
    currentUserId: Int = Session.CURRENT_USER_ID
): ReviewData = ReviewData(
    id = reviewId.toString(),
    productId = articleId.toString(),
    userId = userId,
    authorName = author?.username?.trim() ?: "Usuario",
    timeAgo = createdAt?.toTimeAgo(prefix = "Hace ") ?: "",
    avatarUrl = author?.imageURL,
    avatarRes = null,
    rating = rating.toFloat(),
    reviewText = text?.trim().orEmpty(),
    likesCount = likesCount,
    isLikedByMe = false,
    isMyReview = userId == currentUserId,
    commentsCountText = comments.size.toString(),
    comentariosList = comments
)

/**
 * Comentario -> modelo visual de la pantalla de reseña.
 * `isMyComment` habilita el botón de eliminar y se calcula con la sesión local.
 */
fun CommentDto.toComentarioData(
    author: UserDto? = null,
    currentUserId: Int = Session.CURRENT_USER_ID
): ComentarioReviewData = ComentarioReviewData(
    id = commentId.toString(),
    authorName = author?.username?.trim() ?: "Usuario",
    timeAgo = createdAt?.toTimeAgo(prefix = "Hace ") ?: "",
    avatarUrl = author?.imageURL,
    avatarRes = null,
    commentText = text.trim(),
    likesCount = likesCount,
    isLikedByMe = false,
    isMyComment = userId == currentUserId,
    userId = userId
)
