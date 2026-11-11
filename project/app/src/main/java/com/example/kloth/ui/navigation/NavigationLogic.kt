package com.example.kloth.ui.navigation

object NavigationLogic {

    private val noTopBarScreens = listOf(
        AppRoutes.Login.route,
        AppRoutes.Splash.route,
        AppRoutes.ForgotPassword.route,
        AppRoutes.Register.route,
        AppRoutes.EditProfile.route,
        AppRoutes.FollowList.route,
        AppRoutes.ReviewsMap.route
    )

    private val noBottomBarScreens = listOf(
        AppRoutes.Splash.route,
        AppRoutes.Login.route,
        AppRoutes.Register.route,
        AppRoutes.ForgotPassword.route,
        AppRoutes.ReviewsMap.route
    )

    fun shouldShowTopBar(route: String?) =!noTopBarScreens.contains(route)

    fun shouldShowBottomBar(route: String?) = !noBottomBarScreens.contains(route)
}
