package com.example.kloth.ui.screens.review

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(name = "ReviewScreen - Claro", showBackground = true, showSystemUi = true)
@Composable
fun ReviewScreenPreviewClaro() {
    KlothTheme(darkTheme = false) {
        ReviewScreen()
    }
}

@Preview(
    name = "ReviewScreen - Oscuro",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ReviewScreenPreviewOscuro() {
    KlothTheme(darkTheme = true) {
        ReviewScreen()
    }
}