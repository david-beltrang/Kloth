package com.example.kloth.ui.screens.detail.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun HeroImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    bottomCornerRadius: Dp = 32.dp,
    contentDescription: String = "Imagen principal del producto"
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = bottomCornerRadius, bottomEnd = bottomCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeroImagePreview() {
    KlothTheme {
        HeroImage(imageRes = R.drawable.abrigo_negro)
    }
}