package com.example.kloth.ui.screens.detail.components.descriptionProduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.utils.TranslatableText
import com.example.kloth.ui.utils.TranslationTextState

@Composable
fun ProductDescription(
    descriptionText: String,
    translationState: TranslationTextState = TranslationTextState(),
    onTranslate: () -> Unit = {},
    onShowOriginal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.detail_description_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        TranslatableText(
            originalText = descriptionText,
            state = translationState,
            onTranslate = onTranslate,
            onShowOriginal = onShowOriginal,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 19.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.testTag("detailDescription")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDescriptionPreview() {
    KlothTheme {
        ProductDescription(descriptionText = "Una pieza maestra de sastrería contemporánea...")
    }
}
