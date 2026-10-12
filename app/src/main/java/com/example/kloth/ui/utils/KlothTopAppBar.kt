package com.example.kloth.ui.utils

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
import androidx.compose.ui.graphics.luminance
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
    modifier: Modifier = Modifier
) {
    val logoResource = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        R.drawable.logo_blanco
    } else {
        R.drawable.logo_negro
    }

    CenterAlignedTopAppBar(
        modifier = modifier.height(100.dp),
        title = {
            Image(
                painter = painterResource(id = logoResource),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(130.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Preview(name = "Light Mode")
@Composable
fun KlothTopAppBarPreview() {
    KlothTheme(darkTheme = false) {
        KlothTopAppBar()
    }
}

@Preview(name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KlothTopAppBarDarkPreview() {
    KlothTheme(darkTheme = true) {
        KlothTopAppBar()
    }
}
