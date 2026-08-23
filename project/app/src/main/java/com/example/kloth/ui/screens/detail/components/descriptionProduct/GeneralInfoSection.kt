package com.example.kloth.ui.screens.detail.components.descriptionProduct

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun GeneralInfoSection(
    categoryTag: String,
    price: String,
    title: String,
    brand: String,
    colorName: String,
    categoryName: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ProductHeader(
            categoryTag = categoryTag,
            price = price,
            title = title,
            brand = brand
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProductSpecCards(
            colorName = colorName,
            categoryName = categoryName
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProductDescription(
            descriptionText = description
        )
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GeneralInfoSectionPreview() {
    KlothTheme {
        GeneralInfoSection(
            categoryTag = "Prenda",
            price = "2.450€",
            title = "Abrigo de Cachemira \"Nebulosa\"",
            brand = "Maison de l'Éther",
            colorName = "Nebulosity Dark",
            categoryName = "Outerwear Elite",
            description = "Una pieza maestra de sastrería contemporánea..."
        )
    }
}