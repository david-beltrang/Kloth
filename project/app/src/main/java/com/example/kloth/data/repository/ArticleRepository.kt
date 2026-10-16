package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.ArticleRetrofitDataSourceImpl
import com.example.kloth.data.datasource.implementation.ReviewRetrofitDataSourceImpl
import com.example.kloth.data.dto.toPostItem
import com.example.kloth.data.dto.toProductDetailData
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ProductDetailData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleDataSource: ArticleRetrofitDataSourceImpl,
    private val userRepository: UserRepository,
    private val reviewDataSource: ReviewRetrofitDataSourceImpl
) {

    suspend fun getFeedPosts(): Result<List<PostItem>> = try {
        val posts = coroutineScope {
            val articles = async { articleDataSource.getAllArticles() }
            val users = async { userRepository.getAllUsers() }
            val reviews = async { reviewDataSource.getAllReviews() }

            val usersById = users.await().associateBy { it.userId }
            val reviewsByArticle = reviews.await()
                .filter { it.eliminated != true }
                .groupBy { it.articleId }

            articles.await()
                .filter { !it.eliminated }
                .sortedByDescending { it.createdAt }
                .map { article ->
                    article.toPostItem(
                        creator = usersById[article.idUser],
                        reviews = reviewsByArticle[article.articleId].orEmpty(),
                        usersById = usersById
                    )
                }
        }
        Result.success(posts)
    } catch (e: HttpException) {
        Log.e(TAG, "Error del backend al cargar el feed", e)
        Result.failure(e)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo conectar al cargar el feed", e)
        Result.failure(e)
    }

    suspend fun getArticleDetail(id: String): Result<ProductDetailData> = try {
        val detail = coroutineScope {
            val article = async { articleDataSource.getArticleById(id) }
            val reviews = async { articleDataSource.getArticleReviews(id) }
            val users = async { userRepository.getAllUsers() }

            val usersById = users.await().associateBy { it.userId }
            article.await().toProductDetailData(
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

    private companion object {
        const val TAG = "ArticleRepository"
    }
}
