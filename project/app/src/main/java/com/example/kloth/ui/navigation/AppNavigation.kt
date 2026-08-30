package com.example.kloth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kloth.ui.screens.createArticle.CreateArticleScreen
import com.example.kloth.ui.screens.detail.DetailViewModel
import com.example.kloth.ui.screens.detail.ItemDetailScreen
import com.example.kloth.ui.screens.editProfile.EditProfileScreen
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.feed.FeedViewModel
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordScreen
import com.example.kloth.ui.screens.login.LoginScreen
import com.example.kloth.ui.screens.login.LoginViewModel
import com.example.kloth.ui.screens.notification.NotificationScreen
import com.example.kloth.ui.screens.profile.ProfileScreen
import com.example.kloth.ui.screens.register.RegisterScreen
import com.example.kloth.ui.screens.register.RegisterViewModel
import com.example.kloth.ui.screens.review.ReviewScreen

/**
 * Componente central de navegación de la aplicación.
 * Configura el NavHost y conecta las pantallas mediante callbacks.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route,
        modifier = modifier
    ) {
        // --- Flujo de Autenticación ---
        composable(AppRoutes.Login.route) {
            val loginViewModel: LoginViewModel = viewModel()
            val state by loginViewModel.uiState.collectAsState()

            if (state.navigate) {
                navController.navigate(AppRoutes.Feed.route) {
                    popUpTo(AppRoutes.Login.route) { inclusive = true }
                }
                loginViewModel.onNavigationConsumed()
            }

            LoginScreen(
                loginViewModel = loginViewModel,
                onRegisterClick = {
                    navController.navigate(AppRoutes.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(AppRoutes.ForgotPassword.route)
                }
            )
        }

        composable(AppRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel()
            val state by registerViewModel.uiState.collectAsState()

            if (state.navigate) {
                navController.navigate(AppRoutes.Feed.route) {
                    popUpTo(AppRoutes.Login.route) { inclusive = true }
                }
            }

            RegisterScreen(
                registerViewModel = registerViewModel,
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // --- Flujo Principal de la App ---
        composable(AppRoutes.Feed.route) {
            val feedViewModel: FeedViewModel = viewModel()
            FeedScreen(
                feedViewModel = feedViewModel,
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                }
            )
        }

        composable(AppRoutes.Explore.route) {
            ExploreScreen(
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                }
            )
        }

        composable(AppRoutes.CreateArticle.route) {
            CreateArticleScreen(
                onPublicarClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.Notifications.route) {
            NotificationScreen()
        }

        composable(AppRoutes.Profile.route) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(AppRoutes.EditProfile.route)
                }
            )
        }

        composable(AppRoutes.EditProfile.route) {
            EditProfileScreen(
                onCancelClick = { navController.popBackStack() }
            )
        }

        // --- Pantallas de Detalle y Reseñas ---
        composable(AppRoutes.Review.route) {
            ReviewScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.ArticleDetail.route,
            arguments = listOf(
                navArgument(AppRoutes.ArticleDetail.ARG_PRODUCT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments
                ?.getString(AppRoutes.ArticleDetail.ARG_PRODUCT_ID)
                .orEmpty()

            val detailViewModel: DetailViewModel = viewModel()

            ItemDetailScreen(
                productId = productId,
                detailViewModel = detailViewModel,
                onBackClick = { navController.popBackStack() },
                onWriteReviewClick = {
                    navController.navigate(AppRoutes.Review.route)
                }
            )
        }
    }
}
