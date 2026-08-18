package com.example.kloth.ui.screens.createArticlle.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.ui.theme.KlothTheme

/*
  Estos son los 3 tipos de publicaciones que nuestra app permite crear.
 */
enum class ArticleType {
    PRENDA, OUTFIT, EVENTO
}

/*
 Este es el formulario principal. Cambia su forma dependiendo de qué categoría
 haya elegido el usuario.
 */
@Composable
fun FormArticle(
    selectedType: ArticleType, // Aquí recibimos qué tipo de formulario mostrar
    modifier: Modifier = Modifier
) {
    // Guardamos lo que el usuario escribe en variables "remember"
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    
    // Estos solo se usan si es una Prenda
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    
    // Este solo si es un Outfit
    var estilo by remember { mutableStateOf("") }
    
    // Estos solo si es un Evento
    var ciudad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var organizador by remember { mutableStateOf("") }

    // Usamos una columna con scroll por si el formulario es muy largo
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp) // Espacio parejo entre campos
    ) {
        // campos que siempre
        FormInput(
            label = "Nombre",
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = "Nombre del artículo",
            required = true // Muestra el asterisco rojo
        )

        FormInput(
            label = "Descripción",
            value = descripcion,
            onValueChange = { descripcion = it },
            placeholder = "Describe este artículo...",
            singleLine = false,
            minLines = 4,
            maxLines = 6,
            maxLength = 500 // Muestra el contador de letras abajo
        )

        // campos que cambian segun la categoria
        // AnimatedContent hace que el cambio de campos no sea brusco
        AnimatedContent(targetState = selectedType, label = "form_transition") { type ->
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                when (type) {
                    ArticleType.PRENDA -> {
                        FormInput(label = "Marca", value = marca, onValueChange = { marca = it }, placeholder = "Marca o diseñador")
                        FormInput(label = "Categoría", value = "", onValueChange = {}, placeholder = "Seleccionar categoría", required = true, isSelector = true)
                        FormInput(label = "Color", value = color, onValueChange = { color = it }, placeholder = "Color principal")
                        FormInput(label = "Precio", value = precio, onValueChange = { precio = it }, placeholder = "0.00", prefix = "€", keyboardType = KeyboardType.Number)
                    }
                    ArticleType.OUTFIT -> {
                        FormInput(label = "Estilo", value = estilo, onValueChange = { estilo = it }, placeholder = "Seleccionar estilo", isSelector = true)
                    }
                    ArticleType.EVENTO -> {
                        FormInput(label = "Ciudad", value = ciudad, onValueChange = { ciudad = it }, placeholder = "Ciudad del evento")
                        FormInput(label = "País", value = pais, onValueChange = { pais = it }, placeholder = "País del evento")
                        // Ponemos las fechas una al lado de la otra
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(label = "Inicio", value = "", onValueChange = {}, placeholder = "DD/MM/AA", isSelector = true)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(label = "Fin", value = "", onValueChange = {}, placeholder = "DD/MM/AA", isSelector = true)
                            }
                        }
                        FormInput(label = "Organizador", value = organizador, onValueChange = { organizador = it }, placeholder = "Nombre del organizador")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón final para enviar la publicación
        Button(
            onClick = { /* Aquí iría la lógica para guardar */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Publicar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

/**
 * Este es un componente que creamos para no repetir el diseño de cada cajita de texto.
 * Maneja el título, el asterisco, el contador y el estilo visual.
 */
@Composable
fun FormInput(
    label: String, // Título arriba del campo
    value: String, // El texto actual
    onValueChange: (String) -> Unit, // Función para actualizar el texto
    placeholder: String, // Lo que sale cuando está vacío
    required: Boolean = false, // Si lleva el asterisco rojo
    singleLine: Boolean = true, // Si es de una sola línea
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int? = null, // Límite de letras
    isSelector: Boolean = false, // Si debe mostrar la flechita de bajar
    prefix: String? = null, // Texto al inicio (como el €)
    keyboardType: KeyboardType = KeyboardType.Text // Tipo de teclado (letras o números)
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Título del campo
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            if (required) {
                Text(text = " *", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // La caja de texto propiamente dicha
        TextField(
            value = value,
            onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            prefix = prefix?.let { { Text(text = "$it ") } },
            trailingIcon = if (isSelector) {
                { Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            // Personalizamos colores para que se vea limpio sin rayas pesadas
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                focusedIndicatorColor = Color.Transparent, // Quita la línea de abajo
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp) // Bordes suavizados
        )

        // Si tiene límite de letras, mostramos el contador (ej: 10/500)
        if (maxLength != null) {
            Text(
                text = "${value.length}/$maxLength",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

// --- PREVIEWS PARA VER CÓMO QUEDA CADA UNO EN EL EDITOR ---

@Preview(showBackground = true)
@Composable
fun FormArticlePrendaPreview() {
    KlothTheme {
        FormArticle(selectedType = ArticleType.PRENDA)
    }
}

@Preview(showBackground = true)
@Composable
fun FormArticleOutfitPreview() {
    KlothTheme {
        FormArticle(selectedType = ArticleType.OUTFIT)
    }
}

@Preview(showBackground = true)
@Composable
fun FormArticleEventoPreview() {
    KlothTheme {
        FormArticle(selectedType = ArticleType.EVENTO)
    }
}
