package com.example.kloth.data.repository

import android.content.Context
import android.util.Log
import com.example.kloth.R
import com.example.kloth.data.dataresource.implementation.ArticleRetrofitDataSourceImpl
import com.example.kloth.data.dataresource.implementation.ReviewRetrofitDataSourceImpl
import com.example.kloth.data.dataresource.implementation.UserRetrofitDataSourceImpl
import com.example.kloth.data.dto.toPostItem
import com.example.kloth.data.dto.toProductDetailData
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ProductDetailData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import javax.inject.Inject

// Por ahora se inyectan las implementaciones de Retrofit y no las interfaces (como en la clase 9)
class ArticleRepository @Inject constructor(
    private val articleDataSource: ArticleRetrofitDataSourceImpl,
    private val userDataSource: UserRetrofitDataSourceImpl,
    private val reviewDataSource: ReviewRetrofitDataSourceImpl,
    @ApplicationContext private val context: Context
) {

    // Feed: articulos con su creador, calificacion promedio y numero de resenas.
    // Las tres peticiones van en paralelo (async + await) y se combinan al final.
    suspend fun getFeedPosts(): Result<List<PostItem>> = try {
        val posts = coroutineScope {
            val articles = async { articleDataSource.getAllArticles() }
            val users = async { userDataSource.getAllUsers() }
            val reviews = async { reviewDataSource.getAllReviews() }

            val usersById = users.await().associateBy { it.userId }
            val reviewsByArticle = reviews.await()
                .filter { it.eliminated != true }
                .groupBy { it.articleId }

            articles.await()
                .filter { !it.eliminated }
                .sortedByDescending { it.createdAt } // los mas recientes primero
                .map { article ->
                    article.toPostItem(
                        creator = usersById[article.idUser],
                        reviews = reviewsByArticle[article.articleId].orEmpty(),
                        usersById = usersById
                    )
                }
        }
        Result.success(posts)
    } catch (e: Exception) {
        Result.failure(friendlyError(e))
    }

    // Detalle: el articulo y sus resenas (comentarios), con el nombre y la foto de cada autor
    suspend fun getArticleDetail(id: String): Result<ProductDetailData> = try {
        val detail = coroutineScope {
            val article = async { articleDataSource.getArticleById(id) }
            val reviews = async { articleDataSource.getArticleReviews(id) }
            val users = async { userDataSource.getAllUsers() }

            val usersById = users.await().associateBy { it.userId }
            article.await().toProductDetailData(
                reviews = reviews.await().filter { it.eliminated != true },
                usersById = usersById
            )
        }
        Result.success(detail)
    } catch (e: Exception) {
        Result.failure(friendlyError(e))
    }

    // Mensajes para el usuario en vez de la excepcion cruda (los especificos primero)
    private fun friendlyError(e: Exception): Exception {
        // La excepcion original queda en el Logcat para depurar; al usuario se le muestra un mensaje amigable
        Log.e("ArticleRepository", "Error al consultar el backend", e)
        return userMessage(e)
    }

    private fun userMessage(e: Exception): Exception = when {
        e is HttpException && e.code() == 404 ->
            Exception(context.getString(R.string.network_error_not_found))
        e is HttpException ->
            Exception(context.getString(R.string.network_error_server, e.code()))
        else -> // sin conexion, servidor apagado, tiempo agotado...
            Exception(context.getString(R.string.network_error_connection))
    }
}
