package com.example.kloth.ui.theme

import androidx.compose.ui.graphics.Color

/*
1. Colores primarios (Los más usados)
• KlothBlack: Es el color Primary en modo claro. Se verá en el logo "KLOTH", en los nombres de los usuarios y en los títulos de los posts.
• KlothWhite: Es el color Primary en modo oscuro. También se usa como el color de los iconos cuando están sobre fondos negros.
• KlothSurface: Se usa de fondo en toda la pantalla y en las barras de navegación.

2. Colores de Acento
• RedInferno: Lo usamos específicamente en:
    ◦ El punto de notificación de la campana en la barra superior (KlothTopAppBar).
    ◦ La etiqueta de categoría "COUTURE" en las fotos del feed (KlothBadge).
• Sunset: Se usa exclusivamente para el fondo de los hashtags (como #minimalist) en el pie de los posts para que no distraigan tanto como un color fuerte.


3. Colores de Contenido y Variantes
• KlothOnSurface: Se usa para el texto principal (el cuerpo de los mensajes).
• KlothOnSurfaceVariant: Es un gris. Se usa para textos con menos importancia, como el "2 hours ago" o el número de reviews.
• KlothOutlineVariant: Es el color de las líneas divisoras. Se usa en la línea sutil que separa las pestañas "Following" y "For You".

4. Colores Secundarios (Interacción)
• KlothSecondaryContainer: Se usa para el fondo del ítem seleccionado en la barra de navegación de abajo (Home).
• KlothOnSecondaryContainer: Es el color del icono y el texto cuando están dentro de ese contenedor seleccionado.
 */

val KlothBlack = Color(0xFF020202)
val KlothWhite = Color(0xFFFFFFFF)
val KlothSurface = Color(0xFFFCF8F7)
val KlothOnSurface = Color(0xFF1C1B1B)
val KlothOnSurfaceVariant = Color(0xFF454743)

val RedInferno = Color(0xFF790005)
val Sunset = Color(0xFFE7E7E0)

// Secondary Colors
val KlothSecondary = Color(0xFF52606C)
val KlothSecondaryContainer = Color(0xFFD3E1F0)
val KlothOnSecondaryContainer = Color(0xFF566471)

val KlothOutlineVariant = Color(0xFFC6C7C1)


