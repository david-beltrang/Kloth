package com.example.kloth.ui.navigation

// definicion de rutas de la app
sealed class AppRoutes(val route: String) {

    // login
    object Login : AppRoutes("login")

    // registro
    object Register : AppRoutes("register")

    // recuperar contraseña
    object ForgotPassword : AppRoutes("forgot_password")

    // feed principal (tab 0)
    object Feed : AppRoutes("feed")

    // explorar (tab 1)
    object Explore : AppRoutes("explore")

    // crear publicacion (tab 2)
    object CreateArticle : AppRoutes("create_article")

    // notificaciones (tab 3)
    object Notifications : AppRoutes("notifications")

    // perfil (tab 4)
    object Profile : AppRoutes("profile")

    // editar perfil
    object EditProfile : AppRoutes("edit_profile")

    // resena de articulo, lleva el id como argumento
    object Review : AppRoutes("${RouteConstants.REVIEW_BASE_ROUTE}/{${RouteConstants.REVIEW_ARG_ID}}") {
        const val ARG_REVIEW_ID = RouteConstants.REVIEW_ARG_ID

        fun createRoute(reviewId: String): String = "${RouteConstants.REVIEW_BASE_ROUTE}/$reviewId"
    }

    // detalle de articulo, lleva el id como argumento
    object ArticleDetail : AppRoutes("${RouteConstants.ARTICLE_BASE_ROUTE}/{${RouteConstants.ARTICLE_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = RouteConstants.ARTICLE_ARG_PRODUCT_ID

        fun createRoute(productId: String): String = "${RouteConstants.ARTICLE_BASE_ROUTE}/$productId"
    }

    // crear resena para un producto especifico
    object CreateReview : AppRoutes("${RouteConstants.CREATE_REVIEW_BASE_ROUTE}/{${RouteConstants.CREATE_REVIEW_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = RouteConstants.CREATE_REVIEW_ARG_PRODUCT_ID
        fun createRoute(productId: String): String = "${RouteConstants.CREATE_REVIEW_BASE_ROUTE}/$productId"
    }
}

// Objeto normal para constantes de rutas
object RouteConstants {
    const val ARTICLE_BASE_ROUTE = "article"
    const val ARTICLE_ARG_PRODUCT_ID = "productId"
    const val CREATE_REVIEW_BASE_ROUTE = "create_review"
    const val CREATE_REVIEW_ARG_PRODUCT_ID = "productId"
    const val REVIEW_BASE_ROUTE = "review"
    const val REVIEW_ARG_ID = "reviewId"
}
