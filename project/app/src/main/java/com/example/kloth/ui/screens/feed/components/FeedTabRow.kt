package com.example.kloth.ui.screens.feed.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

/*
Este composable se usa en la pantalla FeedScreen. Es de tipo tab
y sirve para elegir el tipo de contenido que se desea:

- Following
- For you

Es el composable llamado TabRow de Material3
link de referencia: https://www.jetpackcompose.pro/tabs/tabrow/
 */
@Composable
fun FeedTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text(
                    text = stringResource(R.string.tab_following),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text(
                    text = stringResource(R.string.tab_for_you),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}

@Preview
@Composable
fun FeedTabRowPreview() {
    KlothTheme {
        FeedTabRow()
    }
}
