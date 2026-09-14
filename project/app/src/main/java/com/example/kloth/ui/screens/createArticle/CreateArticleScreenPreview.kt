package com.example.kloth.ui.screens.createArticle

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, showSystemUi = true, name = "Light Mode")
@Composable
fun CreateArticleScreenPreview() {
    KlothTheme(darkTheme = false) {
        CreateArticleScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CreateArticleScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        CreateArticleScreen()
    }
}
