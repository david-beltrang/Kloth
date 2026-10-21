  package com.example.kloth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kloth.ui.screens.createArticle.CreateArticleScreen
import com.example.kloth.ui.screens.createReview.CreateReviewScreen
import com.example.kloth.ui.screens.detail.DetailViewModel
import com.example.kloth.ui.screens.detail.ItemDetailScreen
import com.example.kloth.ui.screens.editArticle.EditArticleScreen
import com.example.kloth.ui.screens.editArticle.EditArticleViewModel
import com.example.kloth.ui.screens.editProfile.EditProfileScreen
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.explore.ExploreViewModel
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.feed.FeedViewModel
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordScreen
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordViewModel
import com.example.kloth.ui.screens.login.LoginScreen
import com.example.kloth.ui.screens.login.LoginViewModel
import com.example.kloth.ui.screens.notification.NotificationScreen
import com.example.kloth.ui.screens.notification.NotificationViewModel
import com.example.kloth.ui.screens.profile.ProfileScreen
import com.example.kloth.ui.screens.profile.ProfileViewModel
import com.example.kloth.ui.screens.register.RegisterScreen
import com.example.kloth.ui.screens.register.RegisterViewModel
import com.example.kloth.ui.screens.review.ReviewScreen
import com.example.kloth.ui.screens.splash.SplashScreen
import com.example.kloth.ui.screens.splash.SplashViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Splash.route,
        modifier = modifier
    ) {
        composable(AppRoutes.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            val state by splashViewModel.uiState.collectAsState()

            if (state.navigateToFeed) {
                navController.navigate(AppRoutes.Feed.route) {
                    popUpTo(AppRoutes.Splash.route) { inclusive = true }
                }
                splashViewModel.onNavigationConsumed()
            } else if (state.navigateToLogin) {
                navController.navigate(AppRoutes.Login.route) {
                    popUpTo(AppRoutes.Splash.route) { inclusive = true }
                }
                splashViewModel.onNavigationConsumed()
            }

            SplashScreen(splashViewModel = splashViewModel)
        }

        // --- Flujo de Autenticación ---
        composable(AppRoutes.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
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
            val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
            val state by forgotPasswordViewModel.uiState.collectAsState()

            if (state.navigateBack) {
                navController.popBackStack()
                forgotPasswordViewModel.onNavigationConsumed()
            }

            ForgotPasswordScreen(
                forgotPasswordViewModel = forgotPasswordViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val state by registerViewModel.uiState.collectAsState()

            if (state.navigate) {
                navController.navigate(AppRoutes.Feed.route) {
                    popUpTo(AppRoutes.Login.route) { inclusive = true }
                }
                //Este es el metodo que resetea el navigate a false, sin este metodo
                //la aplicacion se congela, ya que detecta el navigate en true otra vez y hace muchas recompisciones
                registerViewModel.onNavigationConsumed()
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
            val feedViewModel: FeedViewModel = hiltViewModel()
            FeedScreen(
                feedViewModel = feedViewModel,
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                },
                onProfileClick = { userId ->
                    if (userId.isNotBlank()) navController.navigate(AppRoutes.UserProfile.createRoute(userId))
                }
            )
        }

        composable(AppRoutes.Explore.route) {
            val exploreViewModel: ExploreViewModel = hiltViewModel()
            ExploreScreen(
                exploreViewModel = exploreViewModel,
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
            val notificationViewModel: NotificationViewModel = hiltViewModel()
            NotificationScreen(notificationViewModel = notificationViewModel)
        }

        composable(AppRoutes.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val state by profileViewModel.uiState.collectAsState()

            if (state.navigate) {
                navController.navigate(AppRoutes.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                profileViewModel.onNavigationConsumed()
            }

            ProfileScreen(
                viewModel = profileViewModel,
                onEditProfileClick = {
                    navController.navigate(AppRoutes.EditProfile.route)
                },
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                } ,
                onEditArticleClick = { productId ->
                    navController.navigate(AppRoutes.EditArticle.createRoute(productId))
                },
                onDeleteArticleClick = { productId ->
                    // Ejecuta la eliminación en el ViewModel
                    profileViewModel.deleteArticle(productId)
                }

            )
        }

        composable(
            AppRoutes.UserProfile.route,
            arguments = listOf(navArgument(AppRoutes.UserProfile.ARG_USER_ID) { type = NavType.StringType })
        ) { entry ->
            val userId = entry.arguments?.getString(AppRoutes.UserProfile.ARG_USER_ID).orEmpty()
            ProfileScreen(profileUserId = userId, onProductClick = { productId ->
                navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
            })
        }

        composable(AppRoutes.EditProfile.route) {
            EditProfileScreen(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.navigate(AppRoutes.Profile.route) {
                        popUpTo(AppRoutes.Profile.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantallas de Detalle y Reseñas ---
        composable(AppRoutes.Review.route) {
            ReviewScreen(
                onBackClick = { navController.popBackStack() },
                onUserClick = { userId -> navController.navigate(AppRoutes.UserProfile.createRoute(userId)) }
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

            val detailViewModel: DetailViewModel = hiltViewModel()

            ItemDetailScreen(
                productId = productId,
                detailViewModel = detailViewModel,
                onBackClick = { navController.popBackStack() },
                onWriteReviewClick = {
                    navController.navigate(AppRoutes.CreateReview.createRoute(productId))
                },
                onReviewClick = { reviewId ->
                    navController.navigate(AppRoutes.Review.createRoute(reviewId))
                },
                onUserClick = { userId ->
                    navController.navigate(AppRoutes.UserProfile.createRoute(userId))
                }
            )
        }

        composable(
            route = AppRoutes.CreateReview.route,
            arguments = listOf(
                navArgument(AppRoutes.CreateReview.ARG_PRODUCT_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments
                ?.getString(AppRoutes.CreateReview.ARG_PRODUCT_ID)
                .orEmpty()

            CreateReviewScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EditArticle.route,
            arguments = listOf(
                navArgument(AppRoutes.EditArticle.ARG_ARTICLE_ID) { type = NavType.StringType }
            )
        ) {
            val editArticleViewModel: EditArticleViewModel = hiltViewModel()
            EditArticleScreen(
                onSaved = { navController.popBackStack() },
                onCloseClick = {navController.popBackStack()}
            )
        }
    }
}
