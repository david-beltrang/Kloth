package com.example.kloth.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

/** Miniatura de un artículo en la grilla del perfil: solo la imagen, como en el diseño. */
@Composable
fun ProfileArticleTile(
    title: String,
    imageModel: Any?, // Acepta Int (Resource) o String (URL)
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageModel,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileArticleTilePreview() {
    KlothTheme {
        ProfileArticleTile(
            title = "Abrigo negro",
            imageModel = R.drawable.abrigo_negro,
            onClick = {}
        )
    }
}
