package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.data.local.FakeArticle
import com.example.kloth.ui.theme.KlothTheme

// Las previews usan datos de ejemplo: no pueden llamar al backend ni crear el ViewModel con Hilt
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun FeedScreenPreview() {
    KlothTheme(darkTheme = false) {
        FeedScreenContent(mockPosts = FakeArticle.posts, onProductClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeedScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        FeedScreenContent(mockPosts = FakeArticle.posts, onProductClick = {})
    }
}
