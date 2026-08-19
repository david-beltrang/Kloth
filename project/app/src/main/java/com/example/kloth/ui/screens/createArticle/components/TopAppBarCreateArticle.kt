package com.example.kloth.ui.screens.createArticle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun TopAppBarCreateArticle(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.create_article_title)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 8.dp)
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarCreateArticlePreview() {
    KlothTheme {
        TopAppBarCreateArticle()
    }
}
