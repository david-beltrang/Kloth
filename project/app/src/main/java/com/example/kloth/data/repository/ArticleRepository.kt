package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.ArticleFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.retrofit.ReviewRetrofitDataSourceImpl
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.UpdateArticleDto
import com.example.kloth.data.dto.toPostItem
import com.example.kloth.data.dto.withReviews
import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.merge
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val articleDataSource: ArticleFirestoreDataSourceImpl,
    private val userRepository: UserRepository,
    private val reviewDataSource: ReviewRetrofitDataSourceImpl,
    private val storageRepository: StorageRepository
) {
    private val _articlesChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    /** Avisa cada vez que se crea un artículo, para que las pantallas vuelvan a cargar. */
    val articlesChanged: Flow<Unit> = merge(_articlesChanged, userRepository.usersChanged)

    suspend fun getFeedPosts(): Result<List<PostItem>> = try {
        val posts = coroutineScope {
            val articles = async { runCatching { articleDataSource.getAllArticles() }.getOrDefault(emptyList()) }
            val users = async { runCatching { userRepository.getAllUsers() }.getOrDefault(emptyList()) }
            val reviews = async { runCatching { reviewDataSource.getAllReviews() }.getOrDefault(emptyList()) }

            val usersById = users.await().associateBy { it.toUserProfileInfo().id }
            val reviewsByArticle = reviews.await()
                .filter { it.eliminated != true }
                .groupBy { it.articleId.toString() }

            articles.await()
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
        }
        Result.success(posts)
    } catch (e: Throwable) {
        Log.e("TAG", "Error al cargar el feed", e)
        Result.failure(e as? Exception ?: Exception(e))
    }

    suspend fun getArticleDetail(id: String): Result<ArticleInfo> = try {
        val detail = coroutineScope {
            val article = async { articleDataSource.getArticleById(id) }
            val reviews = async { articleDataSource.getArticleReviews(id) }
            val users = async { userRepository.getAllUsers() }

            val usersById = users.await().associateBy { it.toUserProfileInfo().id  }
            article.await().toArticleInfo().withReviews(
                reviews = reviews.await().filter { it.eliminated != true },
                usersById = usersById
            )
        }
        Result.success(detail)

    } catch (e: HttpException) {
        Log.e(TAG, "Error del backend al cargar el articulo $id", e)
        Result.failure(e)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo conectar al cargar el articulo $id", e)
        Result.failure(e)
    }

    suspend fun createArticle(dto: CreateArticleDto): Result<Unit> = try {
        articleDataSource.createArticle(dto)
        _articlesChanged.tryEmit(Unit)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo crear el articulo", e)
        Result.failure(e)
    }

    suspend fun deleteArticle(id: String, imageUrl: String?): Result<Unit> = try {
        articleDataSource.deleteArticle(id)
        // Si la imagen no se puede borrar, el artículo igual queda eliminado
        if (!imageUrl.isNullOrBlank()) storageRepository.deleteImage(imageUrl)
        _articlesChanged.tryEmit(Unit)
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
        _articlesChanged.tryEmit(Unit)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo editar el articulo $id", e)
        Result.failure(e)
    }

    suspend fun getArticleForEdit(articleId: String): Result<ArticleInfo> = try {
        val articleInfo = articleDataSource.getArticleById(articleId).toArticleInfo()
        Result.success(articleInfo)
    } catch (e: HttpException) {
        Log.e(TAG, "Error del backend al cargar el articulo para editar $articleId", e)
        Result.failure(e)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo conectar al cargar el articulo para editar $articleId", e)
        Result.failure(e)
    }

    private companion object {
        const val TAG = "ArticleRepository"
    }
}
