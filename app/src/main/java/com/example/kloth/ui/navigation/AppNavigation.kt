package com.example.kloth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

            LaunchedEffect(state.navigateToFeed, state.navigateToLogin) {
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
            }

            SplashScreen(splashViewModel = splashViewModel)
        }

        composable(AppRoutes.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state by loginViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigate) {
                if (state.navigate) {
                    navController.navigate(AppRoutes.Feed.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                    loginViewModel.onNavigationConsumed()
                }
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

            LaunchedEffect(state.navigateBack) {
                if (state.navigateBack) {
                    navController.popBackStack()
                    forgotPasswordViewModel.onNavigationConsumed()
                }
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

            LaunchedEffect(state.navigate) {
                if (state.navigate) {
                    navController.navigate(AppRoutes.Feed.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                    registerViewModel.onNavigationConsumed()
                }
            }

            RegisterScreen(
                registerViewModel = registerViewModel,
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.Feed.route) {
            val feedViewModel: FeedViewModel = hiltViewModel()
            FeedScreen(
                feedViewModel = feedViewModel,
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
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

            LaunchedEffect(state.navigate) {
                if (state.navigate) {
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    profileViewModel.onNavigationConsumed()
                }
            }

            ProfileScreen(
                viewModel = profileViewModel,
                onEditProfileClick = {
                    navController.navigate(AppRoutes.EditProfile.route)
                },
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                },
                onCommentReviewClick = { reviewId ->
                    navController.navigate(AppRoutes.Review.createRoute(reviewId))
                }
            )
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

        composable(
            route = AppRoutes.UserProfile.route,
            arguments = listOf(
                navArgument(AppRoutes.UserProfile.ARG_USER_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments
                ?.getString(AppRoutes.UserProfile.ARG_USER_ID)
                ?: ""

            LaunchedEffect(userId) {
                if (userId.isBlank()) {
                    navController.popBackStack()
                }
            }

            val userProfileViewModel: ProfileViewModel = hiltViewModel()

            ProfileScreen(
                viewModel = userProfileViewModel,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(AppRoutes.ArticleDetail.createRoute(productId))
                },
                onCommentReviewClick = { reviewId ->
                    navController.navigate(AppRoutes.Review.createRoute(reviewId))
                }
            )
        }

        composable(
            route = AppRoutes.Review.route,
            arguments = listOf(
                navArgument(AppRoutes.Review.ARG_REVIEW_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments
                ?.getString(AppRoutes.Review.ARG_REVIEW_ID)

            ReviewScreen(
                reviewId = reviewId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { productId, reviewId ->
                    navController.navigate(AppRoutes.CreateReview.createRoute(productId, reviewId))
                },
                onProfileClick = { userId ->
                    navController.navigate(AppRoutes.UserProfile.createRoute(userId.toString()))
                }
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
                onCommentReviewClick = { reviewId ->
                    navController.navigate(AppRoutes.Review.createRoute(reviewId))
                },
                onProfileClick = { userId ->
                    navController.navigate(AppRoutes.UserProfile.createRoute(userId.toString()))
                }
            )
        }

        composable(
            route = AppRoutes.CreateReview.route,
            arguments = listOf(
                navArgument(AppRoutes.CreateReview.ARG_PRODUCT_ID) {
                    type = NavType.StringType
                },
                navArgument(AppRoutes.CreateReview.ARG_REVIEW_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments
                ?.getString(AppRoutes.CreateReview.ARG_PRODUCT_ID)
                .orEmpty()
            val reviewId = backStackEntry.arguments
                ?.getString(AppRoutes.CreateReview.ARG_REVIEW_ID)

            CreateReviewScreen(
                productId = productId,
                reviewId = reviewId,
                onBackClick = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.popBackStack()
                }
            )
        }
    }
}
