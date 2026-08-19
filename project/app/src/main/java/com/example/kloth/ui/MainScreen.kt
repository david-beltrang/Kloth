package com.example.kloth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.feed.components.FeedTabRow
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            Column {
                KlothTopAppBar()
                FeedTabRow()
            }
        },
        bottomBar = {
            KlothBottomNavigation(selectedItem = 0)
        }
    ) { innerPadding ->
        FeedScreen(modifier = Modifier.padding(innerPadding))
    }
}
