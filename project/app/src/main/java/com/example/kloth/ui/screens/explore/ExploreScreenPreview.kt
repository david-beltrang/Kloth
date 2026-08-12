package com.example.kloth.ui.screens.explore

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Explore Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ExploreScreenPreview() {
        KlothTheme {
        ExploreScreen()
    }
}