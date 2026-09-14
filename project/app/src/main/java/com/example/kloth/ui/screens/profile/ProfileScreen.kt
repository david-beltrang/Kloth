package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileScreenContent(
            //metodos de estado
            email = uiState.email,
            selectedTabIndex = uiState.selectedTabIndex,
            onTabSelected = { viewModel.onTabSelected(it) },
            //navegacion
            onEditProfileClick = onEditProfileClick,
            onLogoutClick = { viewModel.logOutButtonPressed() },
            modifier = Modifier.fillMaxSize()
        )
    }
}
