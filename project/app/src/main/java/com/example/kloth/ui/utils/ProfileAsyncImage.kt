package com.example.kloth.ui.utils

import com.example.kloth.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProfileAsyncImage(
    profileImage: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        contentDescription = "User Image",
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImage)
            .crossfade(true)
            .build(),


        error = painterResource(id = R.drawable.broken_img),
        placeholder = painterResource(id = R.drawable.loading_icon),
        contentScale = ContentScale.Crop,
        modifier = modifier.size(size.dp).clip(CircleShape)
    )
}