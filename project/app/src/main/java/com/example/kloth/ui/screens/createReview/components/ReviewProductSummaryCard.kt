package com.example.kloth.ui.screens.createReview.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun ReviewProductSummaryCard(
    title: String,
    brand: String,
    variant: String,
    price: String,
    imageRes: Int?,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(175.dp) // Altura exacta para replicar la proporción alta y amplia de tu captura
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                shape = cardShape
            )
    ) {
        // Imagen vertical con ancho definido para mantener la proporción exacta de tu diseño
        AsyncImage(
            model = imageRes ?: imageUrl,
            contentDescription = stringResource(R.string.cd_product_image),
            modifier = Modifier
                .width(140.dp)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )

        // Contenido de texto con el espacio amplio y cómodo de la captura
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = brand,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 21.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = variant,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = price,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(name = "Product Card - Light", showBackground = true)
@Composable
fun ReviewProductSummaryCardLightPreview() {
    KlothTheme(darkTheme = false) {
        ReviewProductSummaryCard(
            title = "Chaqueta Trucker Clásica",
            brand = "LEVI'S · DENIM",
            variant = "Talla M · Azul índigo",
            price = "$89.990 CLP",
            imageRes = R.drawable.abrigo_negro,
            imageUrl = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Product Card - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewProductSummaryCardDarkPreview() {
    KlothTheme(darkTheme = true) {
        ReviewProductSummaryCard(
            title = "Chaqueta Trucker Clásica",
            brand = "LEVI'S · DENIM",
            variant = "Talla M · Azul índigo",
            price = "$89.990 CLP",
            imageRes = R.drawable.abrigo_negro,
            imageUrl = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}