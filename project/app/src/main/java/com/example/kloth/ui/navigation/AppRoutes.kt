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

    /** Reseñas del artículo. */
    object Review : AppRoutes("review")

    /**
     * Detalle de artículo. Requiere el identificador único del artículo,
     * que viaja como argumento dentro de la ruta.
     */
    object ArticleDetail : AppRoutes("${ARTICLE_BASE_ROUTE}/{${ARTICLE_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = ARTICLE_ARG_PRODUCT_ID

        /** Construye la ruta concreta para un artículo específico. */
        fun createRoute(productId: String): String = "${ARTICLE_BASE_ROUTE}/$productId"
    }

    companion object {
        /** Segmento base de la ruta de detalle y nombre de su argumento. */
        private const val ARTICLE_BASE_ROUTE = "article"
        private const val ARTICLE_ARG_PRODUCT_ID = "productId"
    }
}
