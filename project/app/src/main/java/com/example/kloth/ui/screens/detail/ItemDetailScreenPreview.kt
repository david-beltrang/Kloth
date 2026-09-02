package com.example.kloth.ui.screens.detail

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.data.FakeArticle
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Item Detail Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ItemDetailScreenPreview() {
    KlothTheme(darkTheme = false) {
        // Usamos el Content directamente con datos de prueba
        ItemDetailScreenContent(
            product = FakeArticle.abrigoNegro
        )
    }
}

@Preview(
    name = "Item Detail Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemDetailScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        ItemDetailScreenContent(
            product = FakeArticle.abrigoNegro
        )
    }
}