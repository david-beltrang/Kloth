package com.example.kloth.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    modifier: Modifier = Modifier
) {
    val state by splashViewModel.uiState.collectAsState()

    SplashScreenContent(
        isLoading = state.isLoading,
        modifier = modifier
    )
}
