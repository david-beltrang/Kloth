package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ProfileScreenContent(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onEditProfileClick = onEditProfileClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
