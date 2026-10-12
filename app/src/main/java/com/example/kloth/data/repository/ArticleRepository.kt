package com.example.kloth.data.repository

import com.example.kloth.data.dataresource.ArticleRemoteDataSource
import com.example.kloth.data.dataresource.CommentRemoteDataSource
import com.example.kloth.data.dataresource.ReviewRemoteDataSource
import com.example.kloth.data.dto.ArticleDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.toComentarioData
import com.example.kloth.data.dto.toPostItem
import com.example.kloth.data.dto.toProductDetailData
import com.example.kloth.data.dto.toReviewData
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.data.local.ReviewData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Artículos del backend Express.
 *
 * Los autores y las reseñas se resuelven en el cliente (join) porque los
 * endpoints de artículos no incluyen esas relaciones.
 */
@Singleton
class ArticleRepository @Inject constructor(
    private val articleDataSource: ArticleRemoteDataSource,
    private val reviewDataSource: ReviewRemoteDataSource,
    private val commentDataSource: CommentRemoteDataSource,
    private val userRepository: UserRepository
) {

    /** Publicaciones del Feed (artículos + autor + reseñas agrupadas). */
    suspend fun getFeedPosts(): Result<List<PostItem>> {
        return try {
            val articles = articleDataSource.getAllArticles().filter { !it.eliminated }
            val users = userRepository.getUsersMap()

            val reviewsByArticle = reviewDataSource.getAllReviews()
                .filter { !it.eliminated }
                .groupBy { it.articleId }
                .mapValues { (_, reviews) ->
                    reviews.map { it.toReviewData(users[it.userId]) }
                }

            val posts = articles.map { article ->
                article.toPostItem(users[article.idUser], reviewsByArticle[article.articleId].orEmpty())
            }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Etapa 1 del Detail: datos del artículo (sin reseñas). */
    suspend fun getArticleDetail(id: String): Result<ProductDetailData> {
        return try {
            val dto = articleDataSource.getArticleDetail(id)
            Result.success(dto.toProductDetailData())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Etapa 2 del Detail: reseñas del artículo con sus comentarios. */
    suspend fun getArticleReviews(id: String): Result<List<ReviewData>> {
        return try {
            val users = userRepository.getUsersMap()
            val reviews = reviewDataSource.getReviewsByArticle(id).filter { !it.eliminated }

            val reviewList = reviews.map { review ->
                val comments = commentDataSource.getCommentsByReview(review.reviewId.toString())
                    .filter { !it.eliminated }
                    .map { comment -> comment.toComentarioData(users[comment.userId]) }

                review.toReviewData(users[review.userId], comments)
            }
            Result.success(reviewList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Artículos del Explore filtrados por tipo en el cliente. */
    suspend fun getArticlesByType(type: String): Result<List<PostItem>> {
        return getFeedPosts().map { posts -> posts.filter { it.product.categoryTag == type } }
    }

    suspend fun createArticle(dto: CreateArticleDto): Result<Unit> {
        return try {
            articleDataSource.createArticle(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateArticle(id: String, dto: CreateArticleDto): Result<Unit> {
        return try {
            articleDataSource.updateArticle(id, dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteArticle(id: String): Result<Unit> {
        return try {
            articleDataSource.deleteArticle(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticleDto(id: String): Result<ArticleDto> {
        return try {
            Result.success(articleDataSource.getArticleById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Artículos del usuario con autor y reseñas. */
    suspend fun getUserArticles(userId: String): Result<List<PostItem>> {
        return try {
            val articles = articleDataSource.getArticlesByUser(userId).filter { !it.eliminated }
            val users = userRepository.getUsersMap()

            val allReviews = reviewDataSource.getAllReviews().filter { !it.eliminated }
            val reviewsByArticle = allReviews
                .groupBy { it.articleId }
                .mapValues { (_, reviews) ->
                    reviews.map { it.toReviewData(users[it.userId]) }
                }

            val posts = articles.map { article ->
                article.toPostItem(users[article.idUser], reviewsByArticle[article.articleId].orEmpty())
            }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
