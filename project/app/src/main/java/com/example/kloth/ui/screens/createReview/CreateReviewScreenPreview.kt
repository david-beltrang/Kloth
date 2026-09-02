package com.example.kloth.ui.screens.createReview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Create Review Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CreateReviewScreenLightPreview() {
    KlothTheme(darkTheme = false) {
        CreateReviewScreenContent(
            uiState = CreateReviewState(
                rating = 0,
                reviewText = ""
            ),
            onBackClick = {},
            onSubmitClick = {},
            onRatingSelected = {},
            onReviewTextChanged = {}
        )
    }
}

@Preview(
    name = "Create Review Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CreateReviewScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        CreateReviewScreenContent(
            uiState = CreateReviewState(
                rating = 4,
                reviewText = "Excelente chaqueta, la calidad del denim es muy buena."
            ),
            onBackClick = {},
            onSubmitClick = {},
            onRatingSelected = {},
            onReviewTextChanged = {}
        )
    }
}