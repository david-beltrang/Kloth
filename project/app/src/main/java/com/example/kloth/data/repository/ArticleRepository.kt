package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.ArticleFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.UpdateArticleDto
import com.example.kloth.data.dto.toPostItem
import com.example.kloth.data.dto.withReviews
import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val articleDataSource: ArticleFirestoreDataSourceImpl,
    private val userRepository: UserRepository,
    private val reviewDataSource: ReviewFirestoreDataSourceImpl,
    private val storageRepository: StorageRepository
) {

    suspend fun getFeedPosts(): Result<List<PostItem>> = try {
        // Los datos se traen de forma secuencial: primero el artículo, luego su autor y sus reseñas.
        val articles = articleDataSource.getAllArticles()
        val users = userRepository.getAllUsers()
        val reviews = reviewDataSource.getAllReviews()

        val usersById = users.associateBy { it.toUserProfileInfo().id }
        val reviewsByArticle = reviews
            .filter { it.eliminated != true }
            .groupBy { it.articleIdKey() }

        val posts = articles
            .map { it.toArticleInfo() }
            .filter { !it.eliminated }
            .sortedByDescending { it.createdAt }
            .map { article ->
                article.toPostItem(
                    creator = usersById[article.creatorId],
                    reviews = reviewsByArticle[article.id].orEmpty(),
                    usersById = usersById
                )
            }
        Result.success(posts)
    } catch (e: Exception) {
        Log.e(TAG, "Error al cargar el feed", e)
        Result.failure(e)
    }

    suspend fun getArticleDetail(id: String): Result<ArticleInfo> = try {
        val article = articleDataSource.getArticleById(id)
        val reviews = articleDataSource.getArticleReviews(id)
        val users = userRepository.getAllUsers()

        val usersById = users.associateBy { it.toUserProfileInfo().id }
        val detail = article.toArticleInfo().withReviews(
            reviews = reviews.filter { it.eliminated != true },
            usersById = usersById
        )
        Result.success(detail)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo cargar el articulo $id", e)
        Result.failure(e)
    }

    suspend fun createArticle(dto: CreateArticleDto): Result<Unit> = try {
        articleDataSource.createArticle(dto)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo crear el articulo", e)
        Result.failure(e)
    }

    suspend fun deleteArticle(id: String, imageUrl: String?): Result<Unit> = try {
        articleDataSource.deleteArticle(id)
        // Si la imagen no se puede borrar, el artículo igual queda eliminado
        if (!imageUrl.isNullOrBlank()) storageRepository.deleteImage(imageUrl)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo eliminar el articulo $id", e)
        Result.failure(e)
    }

    suspend fun updateArticle(id: String, dto: UpdateArticleDto, oldImageUrl: String?): Result<Unit> = try {
        articleDataSource.updateArticle(id, dto)
        // Si la imagen cambió, la anterior ya no se usa. Si no se puede borrar, no importa
        if (!oldImageUrl.isNullOrBlank() && oldImageUrl != dto.imageArticle) {
            storageRepository.deleteImage(oldImageUrl)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo editar el articulo $id", e)
        Result.failure(e)
    }

    suspend fun getArticleForEdit(articleId: String): Result<ArticleInfo> = try {
        val articleInfo = articleDataSource.getArticleById(articleId).toArticleInfo()
        Result.success(articleInfo)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo cargar el articulo para editar $articleId", e)
        Result.failure(e)
    }

    private companion object {
        const val TAG = "ArticleRepository"
    }
}