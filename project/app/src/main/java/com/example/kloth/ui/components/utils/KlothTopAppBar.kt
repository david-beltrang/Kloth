package com.example.kloth.ui.components.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.theme.RedInferno

/*
Este composable se usa en la pantalla FeedScreen y funciona como barra de navegacion.
Es de tipo CenterAlignedTopAppBar, Su característica es que el título de la pantalla se alinea de forma automática en el centro horizontal de la barra

link de referencia: https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-center-aligned-top-app-bar.html
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlothTopAppBar(
    modifier: Modifier = Modifier,
    notificationCount: Int = 0

) {
    CenterAlignedTopAppBar(
        modifier = modifier.height(100.dp),
        title = {
            Image(
                painter = painterResource(id = R.drawable.klogo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(130.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.content_desc_back)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.content_desc_search)
                )
            }
            BadgedBox(
                badge = {
                    if (notificationCount > 0) {
                        Badge(
                            containerColor = RedInferno,
                            contentColor = Color.White,
                            modifier = Modifier
                                .size(14.dp)
                                .offset(x = (-4).dp, y = 4.dp) // Movemos el badge hacia adentro
                        ) {
                            Text(
                                text = notificationCount.toString(),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            ) {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(id = R.string.content_desc_notifications)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Preview
@Composable
fun KlothTopAppBarPreview() {
    KlothTheme {
        KlothTopAppBar(notificationCount = 5)
    }
}
