package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun FeedScreenPreview() {
    KlothTheme(darkTheme = false) {
        FeedScreen(feedViewModel = viewModel())
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FeedScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        FeedScreen(feedViewModel = viewModel())
    }
}
