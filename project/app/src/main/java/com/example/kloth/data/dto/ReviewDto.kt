package com.example.kloth.data.dto

import com.example.kloth.R
import com.example.kloth.data.local.Coordinates
import com.example.kloth.data.local.ReviewData
import com.example.kloth.data.local.ReviewMapPin

abstract class ReviewDtoGeneric {
    abstract val rating: Int
    abstract val eliminated: Boolean
    abstract val createdAt: String?
    abstract val latitude: Double?
    abstract val longitude: Double?

    // Express usa ids Int y Firestore String: ambos se exponen como String
    abstract fun articleIdKey(): String
    abstract fun authorIdKey(): String

    abstract fun toReviewData(author: UserDtoGeneric?): ReviewData

    // Devuelve null si el review no tiene coordenadas (ubicación opcional)
    abstract fun toReviewMapPin(author: UserDtoGeneric?): ReviewMapPin?

    // Filtro defensivo: si el parseo del timestamp falla, se excluye el review del mapa
    fun isCreatedAtOrAfter(since: java.time.Instant): Boolean {
        return try {
            val created = java.time.Instant.parse(createdAt ?: return false)
            !created.isBefore(since)
        } catch (e: Exception) {
            false
        }
    }
}

data class ReviewRetrofitDto(
    val reviewId: Int,
    val userId: Int,
    val articleId: Int,
    override val rating: Int,
    val text: String?,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    override val createdAt: String? = null,
    val updatedAt: String? = null,
    override val eliminated: Boolean = false,
    override val latitude: Double? = null,
    override val longitude: Double? = null
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

    override fun toReviewMapPin(author: UserDtoGeneric?): ReviewMapPin? {
        val lat = latitude ?: return null
        val lng = longitude ?: return null
        val profile = author?.toUserProfileInfo()
        return ReviewMapPin(
            reviewId = reviewId.toString(),
            articleId = articleId.toString(),
            authorId = userId.toString(),
            authorName = profile?.username?.trim() ?: "Usuario eliminado",
            avatarUrl = profile?.profileImageUrl,
            rating = rating.toFloat(),
            reviewText = text?.trim().orEmpty(),
            timeAgo = timeAgo(createdAt),
            coordinates = Coordinates(lat, lng)
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
    override val createdAt: String? = null,
    val updatedAt: String? = null,
    override val eliminated: Boolean = false,
    override val latitude: Double? = null,
    override val longitude: Double? = null
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

    override fun toReviewMapPin(author: UserDtoGeneric?): ReviewMapPin? {
        val lat = latitude ?: return null
        val lng = longitude ?: return null
        val profile = author?.toUserProfileInfo()
        return ReviewMapPin(
            reviewId = reviewId,
            articleId = articleId,
            authorId = userId,
            authorName = profile?.username?.trim() ?: "Usuario eliminado",
            avatarUrl = profile?.profileImageUrl,
            rating = rating.toFloat(),
            reviewText = text?.trim().orEmpty(),
            timeAgo = timeAgo(createdAt),
            coordinates = Coordinates(lat, lng)
        )
    }
}

data class CreateReviewDto(
    val userId: String,
    val articleId: String,
    val rating: Int,
    val text: String?,
    val createdAt: String,
    val eliminated: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class UpdateReviewDto(
    val rating: Int? = null,
    val text: String? = null,
    val updatedAt: String? = null,
    val eliminated: Boolean? = null
)
