package com.example.kloth.ui.navigation

sealed class AppRoutes(val route: String) {

    object Splash : AppRoutes("splash")

    object Login : AppRoutes("login")

    object Register : AppRoutes("register")

    object ForgotPassword : AppRoutes("forgot_password")

    object Feed : AppRoutes("feed")

    object Explore : AppRoutes("explore")

    object CreateArticle : AppRoutes("create_article")

    object Notifications : AppRoutes("notifications")

    object Profile : AppRoutes("profile")

    object UserProfile : AppRoutes("${RouteConstants.USER_PROFILE_BASE_ROUTE}/{${RouteConstants.USER_PROFILE_ARG_ID}}") {
        const val ARG_USER_ID = RouteConstants.USER_PROFILE_ARG_ID

        fun createRoute(userId: String): String = "${RouteConstants.USER_PROFILE_BASE_ROUTE}/$userId"
    }

    object EditProfile : AppRoutes("edit_profile")

    object Review : AppRoutes("${RouteConstants.REVIEW_BASE_ROUTE}/{${RouteConstants.REVIEW_ARG_ID}}") {
        const val ARG_REVIEW_ID = RouteConstants.REVIEW_ARG_ID

        fun createRoute(reviewId: String): String = "${RouteConstants.REVIEW_BASE_ROUTE}/$reviewId"
    }

    object ArticleDetail : AppRoutes("${RouteConstants.ARTICLE_BASE_ROUTE}/{${RouteConstants.ARTICLE_ARG_PRODUCT_ID}}") {
        const val ARG_PRODUCT_ID = RouteConstants.ARTICLE_ARG_PRODUCT_ID

        fun createRoute(productId: String): String = "${RouteConstants.ARTICLE_BASE_ROUTE}/$productId"
    }

    object CreateReview : AppRoutes("${RouteConstants.CREATE_REVIEW_BASE_ROUTE}/{${RouteConstants.CREATE_REVIEW_ARG_PRODUCT_ID}}?reviewId={reviewId}") {
        const val ARG_PRODUCT_ID = RouteConstants.CREATE_REVIEW_ARG_PRODUCT_ID
        const val ARG_REVIEW_ID = "reviewId"
        fun createRoute(productId: String, reviewId: String? = null): String {
            return if (reviewId != null) {
                "${RouteConstants.CREATE_REVIEW_BASE_ROUTE}/$productId?reviewId=$reviewId"
            } else {
                "${RouteConstants.CREATE_REVIEW_BASE_ROUTE}/$productId"
            }
        }
    }
}

object RouteConstants {
    const val ARTICLE_BASE_ROUTE = "article"
    const val ARTICLE_ARG_PRODUCT_ID = "productId"
    const val CREATE_REVIEW_BASE_ROUTE = "create_review"
    const val CREATE_REVIEW_ARG_PRODUCT_ID = "productId"
    const val REVIEW_BASE_ROUTE = "review"
    const val REVIEW_ARG_ID = "reviewId"
    const val USER_PROFILE_BASE_ROUTE = "user_profile"
    const val USER_PROFILE_ARG_ID = "userId"
}
