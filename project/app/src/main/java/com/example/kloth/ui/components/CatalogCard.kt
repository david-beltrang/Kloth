package com.example.kloth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R

@Composable
// Tarjeta de producto del catálogo: imagen, etiqueta de categoría, título, calificación y favorito.
fun CatalogCard(
    title: String,
    category: String,
    rating: String,
    reviews: String,
    imageRes: Int, // <-- Nuevo parámetro para recibir la imagen
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Área de Imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.surface_container_low))
        ) {
            // Renderizado de la imagen
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop, // Recorta y para llenar la tarjeta sin deformarse
                modifier = Modifier.fillMaxSize()
            )

            // Botón flotante de corazon (Agregar a Favoritos)
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.cd_favorite),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Etiqueta de categoría
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(colorResource(id = R.color.sunset))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = category.uppercase(),
                color = colorResource(id = R.color.on_surface),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Título del producto
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.on_surface),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Calificación y reseñas
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = colorResource(id = R.color.primary),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = rating,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "($reviews)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.astronomic_grey)
            )
        }
    }
}