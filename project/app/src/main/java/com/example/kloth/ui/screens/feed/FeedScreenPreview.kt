package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    KlothTheme {
        FeedScreen(navController = rememberNavController())
    }
}
