package com.example.kloth.ui.utils

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.kloth.R
import com.example.kloth.data.repository.resolveApplicationLanguage
import java.util.Locale

/** La app admite español, inglés, portugués, francés, alemán, italiano, ruso y árabe. */
fun appTranslationLanguage(context: Context): String {
    val selectedLanguage = AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag()
    val configuration = context.resources.configuration
    val systemLanguage = (configuration.locales?.get(0) ?: configuration.locale)?.toLanguageTag()
    return resolveApplicationLanguage(selectedLanguage, systemLanguage)
}

/** Obtiene las opciones canónicas en español, independientemente del idioma visible. */
fun spanishStringArray(context: Context, @ArrayRes arrayRes: Int): List<String> {
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(Locale.forLanguageTag("es"))
    return context.createConfigurationContext(configuration).resources.getStringArray(arrayRes).toList()
}

/** Traduce las etiquetas de categorías del catálogo por posición y conserva valores antiguos. */
@Composable
fun localizedArticleOption(value: String, @ArrayRes arrayRes: Int): String {
    val canonicalOptions = spanishStringArray(androidx.compose.ui.platform.LocalContext.current, arrayRes)
    val localizedOptions = stringArrayResource(arrayRes)
    val index = canonicalOptions.indexOf(value)
    return localizedOptions.getOrNull(index) ?: value
}

/** Traduce los tipos propios de Kloth y deja intactas las etiquetas históricas desconocidas. */
@Composable
fun localizedArticleType(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "prenda" -> stringResource(R.string.type_prenda)
    "outfit" -> stringResource(R.string.type_outfit)
    "evento" -> stringResource(R.string.type_evento)
    "marca" -> stringResource(R.string.type_marca)
    else -> value
}
