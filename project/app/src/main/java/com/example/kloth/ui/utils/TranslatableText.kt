package com.example.kloth.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.kloth.R

/** Muestra texto traducible sin alterar su tipografía ni su jerarquía visual. */
@Composable
fun TranslatableText(
    originalText: String,
    state: TranslationTextState,
    onTranslate: () -> Unit,
    onShowOriginal: () -> Unit,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val displayedText = (state.status as? TranslationUiStatus.Translated)?.text ?: originalText
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = displayedText, style = style, color = color)
        if (state.canTranslate || state.status is TranslationUiStatus.Translated) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = if (state.status is TranslationUiStatus.Translated) onShowOriginal else onTranslate,
                    enabled = state.status !is TranslationUiStatus.Translating
                ) {
                    Text(
                        text = stringResource(
                            if (state.status is TranslationUiStatus.Translated) {
                                R.string.view_original_button
                            } else R.string.translate_button
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                if (state.status is TranslationUiStatus.Translating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
            if (state.status is TranslationUiStatus.Error) {
                Text(
                    text = stringResource(R.string.translation_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
