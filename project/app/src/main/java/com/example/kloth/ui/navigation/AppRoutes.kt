package com.example.kloth.ui.navigation

/**
 * Definición centralizada y tipada de todas las rutas de la aplicación.
 *
 * Las Screens nunca conocen estas rutas directamente: solo reciben callbacks
 * (onProductClick, onBackClick, etc.). El único punto que las utiliza es
 * AppNavHost junto con el Scaffold principal de MainActivity.
 */
sealed class AppRoutes(val route: String) {

    /** Pantalla de inicio de sesión (destino inicial). */
    object Login : AppRoutes("login")

    /** Pantalla de registro. */
    object Register : AppRoutes("register")

    /** Pantalla de recuperación de contraseña. */
    object ForgotPassword : AppRoutes("forgot_password")

    /** Feed principal (tab 0). */
    object Feed : AppRoutes("feed")

    /** Explorar catálogo (tab 1). */
    object Explore : AppRoutes("explore")

    /** Crear publicación (tab 2). */
    object CreateArticle : AppRoutes("create_article")

    /** Notificaciones (tab 3). */
    object Notifications : AppRoutes("notifications")

    /** Perfil de usuario (tab 4). */
    object Profile : AppRoutes("profile")

    /** Edición de perfil. */
    object EditProfile : AppRoutes("edit_profile")

    /** Reseñas del artículo. Requiere el identificador único de la reseña. */
    object Review : AppRoutes("${REVIEW_BASE_ROUTE}/{${REVIEW_ARG_ID}}") {
        const val ARG_REVIEW_ID = REVIEW_ARG_ID

        /** Construye la ruta concreta para una reseña específica. */
        fun createRoute(reviewId: String): String = "${REVIEW_BASE_ROUTE}/$reviewId"
    }

    /**
     * Detalle de artículo. Requiere el identificador único del artículo,
     * que viaja como argumento dentro de la ruta.
     */
    object ArticleDetail : AppRoutes("${ARTICLE_BASE_ROUTE}/{${ARTICLE_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = ARTICLE_ARG_PRODUCT_ID

        /** Construye la ruta concreta para un artículo específico. */
        fun createRoute(productId: String): String = "${ARTICLE_BASE_ROUTE}/$productId"
    }

    /** Pantalla para redactar una nueva reseña de un producto específico. */
    object CreateReview : AppRoutes("${CREATE_REVIEW_BASE_ROUTE}/{${CREATE_REVIEW_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = CREATE_REVIEW_ARG_PRODUCT_ID

        fun createRoute(productId: String): String = "${CREATE_REVIEW_BASE_ROUTE}/$productId"
    }

    companion object {
        /** Segmento base y argumentos de rutas parametrizadas. */
        private const val ARTICLE_BASE_ROUTE = "article"
        private const val ARTICLE_ARG_PRODUCT_ID = "productId"
        private const val CREATE_REVIEW_BASE_ROUTE = "create_review"
        private const val CREATE_REVIEW_ARG_PRODUCT_ID = "productId"
        private const val REVIEW_BASE_ROUTE = "review"
        private const val REVIEW_ARG_ID = "reviewId"
    }
}