package com.example.kloth.data.dto

import com.example.kloth.R
import com.example.kloth.data.local.ReviewData

abstract class ReviewDtoGeneric {
    abstract val rating: Int
    abstract val eliminated: Boolean

    // Express usa ids Int y Firestore String: ambos se exponen como String
    abstract fun articleIdKey(): String
    abstract fun authorIdKey(): String

    abstract fun toReviewData(author: UserDtoGeneric?): ReviewData
}

data class ReviewRetrofitDto(
    val reviewId: Int,
    val userId: Int,
    val articleId: Int,
    override val rating: Int,
    val text: String?,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: String?,
    val updatedAt: String? = null,
    override val eliminated: Boolean = false
) : ReviewDtoGeneric() {

    override fun articleIdKey(): String = articleId.toString()
    override fun authorIdKey(): String = userId.toString()

    override fun toReviewData(author: UserDtoGeneric?): ReviewData {
        val authorProfile = author?.toUserProfileInfo()
        return ReviewData(
            id = reviewId.toString(),
            authorId = userId.toString(),
            authorName = authorProfile?.username?.trim() ?: "Usuario eliminado",
            timeAgo = timeAgo(createdAt),
            avatarRes = R.drawable.profile,
            avatarUrl = authorProfile?.profileImageUrl,
            rating = rating.toFloat(),
            reviewText = text?.trim().orEmpty(),
            likesCount = likesCount,
            commentsCountText = commentsCount.toString()
        )
    }
}

data class ReviewFirestoreDto(
    val reviewId: String = "",
    val userId: String = "",
    val articleId: String = "",
    override val rating: Int = 0,
    val text: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    override val eliminated: Boolean = false
) : ReviewDtoGeneric() {

    override fun articleIdKey(): String = articleId
    override fun authorIdKey(): String = userId

    override fun toReviewData(author: UserDtoGeneric?): ReviewData {
        val authorProfile = author?.toUserProfileInfo()
        return ReviewData(
            id = reviewId,
            authorId = userId,
            authorName = authorProfile?.username?.trim() ?: "Usuario eliminado",
            timeAgo = timeAgo(createdAt),
            avatarRes = R.drawable.profile,
            avatarUrl = authorProfile?.profileImageUrl,
            rating = rating.toFloat(),
            reviewText = text?.trim().orEmpty(),
            likesCount = likesCount,
            commentsCountText = commentsCount.toString()
        )
    }
}

data class CreateReviewDto(
    val userId: String,
    val articleId: String,
    val rating: Int,
    val text: String?,
    val createdAt: String,
    val eliminated: Boolean = false
)

data class UpdateReviewDto(
    val rating: Int? = null,
    val text: String? = null,
    val updatedAt: String? = null,
    val eliminated: Boolean? = null
)
