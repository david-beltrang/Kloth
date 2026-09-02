package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ProfileScreenContent(
            selectedTabIndex = uiState.selectedTabIndex,
            onTabSelected = { viewModel.onTabSelected(it) },
            onEditProfileClick = onEditProfileClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
