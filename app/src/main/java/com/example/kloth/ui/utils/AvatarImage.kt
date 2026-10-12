package com.example.kloth.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kloth.R

/**
 * Avatar circular. Prefiere la URL remota del backend y, si no existe,
 * cae en el recurso drawable local.
 */
@Composable
fun AvatarImage(
    url: String?,
    @DrawableRes fallbackRes: Int?,
    contentDescription: String?,
    size: Int,
    modifier: Modifier = Modifier
) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            error = painterResource(id = R.drawable.broken_img),
            placeholder = painterResource(id = R.drawable.loading_icon),
            contentScale = ContentScale.Crop,
            modifier = modifier.size(size.dp).clip(CircleShape)
        )
    } else {
        Image(
            painter = painterResource(id = fallbackRes ?: R.drawable.profile),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(size.dp).clip(CircleShape)
        )
    }
}
