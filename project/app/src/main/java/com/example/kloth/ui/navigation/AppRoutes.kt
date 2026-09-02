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
    object Review : AppRoutes("${REVIEW_BASE_ROUTE}/{${REVIEW_ARG_ID}}") {
        const val ARG_REVIEW_ID = REVIEW_ARG_ID

        fun createRoute(reviewId: String): String = "${REVIEW_BASE_ROUTE}/$reviewId"
    }

    // detalle de articulo, lleva el id como argumento
    object ArticleDetail : AppRoutes("${ARTICLE_BASE_ROUTE}/{${ARTICLE_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = ARTICLE_ARG_PRODUCT_ID

        fun createRoute(productId: String): String = "${ARTICLE_BASE_ROUTE}/$productId"
    }

    // crear resena para un producto especifico
    object CreateReview : AppRoutes("${CREATE_REVIEW_BASE_ROUTE}/{${CREATE_REVIEW_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = CREATE_REVIEW_ARG_PRODUCT_ID

        fun createRoute(productId: String): String = "${CREATE_REVIEW_BASE_ROUTE}/$productId"
    }

    companion object {
        // constantes para rutas con argumentos
        private const val ARTICLE_BASE_ROUTE = "article"
        private const val ARTICLE_ARG_PRODUCT_ID = "productId"
        private const val CREATE_REVIEW_BASE_ROUTE = "create_review"
        private const val CREATE_REVIEW_ARG_PRODUCT_ID = "productId"
        private const val REVIEW_BASE_ROUTE = "review"
        private const val REVIEW_ARG_ID = "reviewId"
    }
}