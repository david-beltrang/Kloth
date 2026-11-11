# Plan de Implementación: Botón "Try On" en Feed, Item Detail y Tab "Model Pics"

**Proyecto:** Kloth (App de moda en Kotlin, Jetpack Compose, MVVM, Firebase)  
**Fecha:** 08 de noviembre de 2026  
**Estado:** Implementación incremental; Cloud Function planificada para la última fase

---

## 1. Objetivo

Integrar el botón **"Try On"** en:
1. Cada publicación del **feed** (pestañas *Following* y *For You*).
2. La pantalla de **item detail** (detalle de la publicación).

Al pulsar "Try On", se abre un **popup grande con fondo borroso** donde el usuario selecciona una de sus imágenes base ("model pics"), ejecuta el try-on con la API de **FASHN**, y al finalizar navega al tab **"Model Pics"** del perfil para ver la imagen generada.

---

## 2. Stack Tecnológico

| Capa | Tecnología |
|------|------------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose |
| Arquitectura | MVVM + StateFlow |
| Navegación | Navigation Compose |
| Autenticación | Firebase Auth |
| Base de datos | Firestore |
| Almacenamiento de imágenes | Firebase Storage |
| Lógica de servidor | Cloud Functions (2nd gen, HTTPS Callable) |
| IA Try-On | API de FASHN |
| Manejo de red | Retrofit + OkHttp (solo en Cloud Function) |
| Imágenes en UI | Coil (o Accompanist / Compose Image) |

---

## 3. Arquitectura (MVVM)

```
View (Compose: Feed / Item Detail / Profile) ←→ ViewModel (StateFlow) ←→ Repository ←→ Firebase / Cloud Function
```

- **View:** Observa el `StateFlow` del ViewModel y renderiza UI reactiva.
- **ViewModel:** Contiene lógica de presentación (estados de carga, selección de imagen, navegación).
- **Repository:** Abstrae llamadas a Firestore, Storage y Cloud Function.
- **Cloud Function:** Orquesta la llamada a FASHN, sube el resultado a Storage y guarda metadatos en Firestore. La API Key de FASHN vive exclusivamente aquí.

---

## 4. Modelo de Datos

### Subcolección Firestore: `users/{userId}/modelPics/{modelPicId}`

Se guarda dentro del documento del propietario para que las consultas del perfil y las reglas de acceso queden delimitadas por UID. La imagen binaria vive en Firebase Storage; Firestore guarda sus metadatos:

```json
{
  "name": "string (ej. 'Foto cuerpo completo')",
  "imageUrl": "string (URL de Firebase Storage)",
  "storagePath": "string (ruta del archivo dentro de Storage)",
  "type": "string ('base' | 'generated')",
  "sourcePublicationId": "string|null (publicación de la prenda probada)",
  "baseModelPicId": "string|null (imagen base usada para generar el resultado)",
  "createdAt": "timestamp (server timestamp)"
}
```

El ID del documento es el ID de la model pic; no se duplica como campo. Para imágenes base, `sourcePublicationId` y `baseModelPicId` son `null`. Para resultados, `type` es `generated` y ambos campos identifican el origen.

---

## 5. Cloud Function: `generarTryOn`

**Tipo:** HTTPS Callable de Firebase Functions v2  
**Entrada callable:**
- `baseModelPicId`: ID de la Model Pic base seleccionada.
- `publicationId`: ID de la publicación de la prenda/outfit.
- El UID se obtiene del contexto autenticado de Firebase; el cliente no envía URLs ni `userId`.

**Flujo interno:**
1. Validar que el usuario esté autenticado, que la foto base pertenezca a ese UID y que la publicación sea `prenda` u `outfit`.
2. Leer en Firestore la ruta propia de la foto base y la URL de la publicación; validar la ruta en Storage y construir una URL de descarga para FASHN. El cliente no sustituye estos datos por URLs arbitrarias.
3. Hacer una sola solicitud a `tryon-max`, con `model_image`, `product_image`, `num_images: 1`, `resolution: "1k"`, `generation_mode: "fast"` y `output_format: "jpeg"`.
4. Elegir `prompt` por `typeArticle`: para `outfit`, pedir que vista el conjunto completo visible; para `prenda`, pedir que cambie únicamente el artículo correspondiente y conserve el resto del outfit.
5. Consultar dentro de la función el prediction ID de FASHN hasta completarse o fallar, con timeout/backoff; el polling no inicia nuevas generaciones.
6. Subir el resultado a Firebase Storage (`modelPics/{userId}/{uuid}`).
7. Crear el documento `type: "generated"` en `users/{userId}/modelPics` con `sourcePublicationId` y `baseModelPicId`.
8. Devolver al cliente `modelPicId` y `imageUrl`; el cliente pasa de carga a resultado sin una colección de jobs intermedia.

**Seguridad:**
- La API Key se lee de `process.env.FASHN_API_KEY` en Cloud Functions y nunca se incluye en Android. En desarrollo local se usa el `.env` ignorado de `CloudFunctions/functions`; para producción se debe configurar como variable del entorno de Functions (Secret Manager es la opción más segura si se decide migrar).
- Solo usuarios autenticados pueden invocar la función.

---

## 6. Pantallas y Componentes Compose

### 6.1 Feed (Following y For You)

Cada publicación de tipo **prenda** u **outfit** en el feed incluye:
- Imagen de la prenda/outfit.
- Información de la publicación.
- **Botón compacto "Try On ✨"** (visible solo para prendas y outfits).

Al pulsar el botón:
- Se abre el `TryOnDialog` pasando el `publicationId` y la URL de la prenda.

### 6.2 Item Detail (detalle de publicación)

Pantalla completa de una publicación con:
- Imagen grande de la prenda.
- Descripción, precio, talla, etc.
- **Botón compacto "Try On ✨"** ubicado en lugar visible, solo para publicaciones de prendas y outfits.
- Al pulsar: se abre el mismo `TryOnDialog` con los datos de esa publicación.

### 6.3 Pantalla Profile — Tab "Model Pics"

Este tab sirve como **gestor de model pics** y **destino final del flujo try-on**.

Estructura con `TabRow` interno:
| Tab | Contenido |
|-----|-----------|
| Publicaciones | Lista de publicaciones del usuario |
| Artículos calificados | Prendas/outfits calificados por el usuario |
| **Model Pics** | Grid de imágenes base y generadas |

#### Dentro del tab Model Pics:

1. **Dos botones de filtro:**
   - "Subidas por mí" (`type == 'base'`)
   - "Generadas por IA" (`type == 'generated'`)

2. **Grid de imágenes (2 columnas):**
   - `LazyVerticalGrid(columns = GridCells.Fixed(2))`
   - Cada ítem: imagen + nombre debajo.

3. **Estados del ViewModel:**
   - `modelPics: List<ModelPic>`
   - `filtroActivo: FiltroModelPic` (BASE / GENERADA / TODOS)

---

## 7. Popup / Dialog de Try-On

Se activa desde **cualquier publicación del feed o del item detail**.

**Características:**
- `Dialog` o `AlertDialog` personalizado, tamaño grande (aprox. 80-90% de ancho y alto de pantalla).
- Fondo de pantalla borroso/dimmed.
- Se recibe como parámetro: `publicationId` y `urlPrenda`.

**Contenido interno del popup:**
1. Lista de imágenes model pics del usuario (tipo `base`) en grid o lista vertical.
2. Cada ítem con `Checkbox` a la derecha (selección única).
3. Solo se permite seleccionar **una** imagen a la vez.

**Estados internos del popup:**

| Estado | Qué se muestra |
|--------|----------------|
| `idle` | Lista de imágenes base + checkboxes |
| `seleccionada` | Lista + botón "Try On ✨" habilitado |
| `generando` | Mensaje "Generando imagen" + `CircularProgressIndicator` |
| `listo` | Imagen generada + botón "Continuar" |
| `error` | Mensaje de error + botón "Reintentar" |

**Botón "Try On ✨":**
- Aparece solo cuando hay una imagen base seleccionada.
- Texto: `Try On` + icono de estrellas mágicas (✨).

**Acción "Continuar":**
1. Cierra el popup.
2. Navega al tab **"Model Pics"** del profile.
3. Activa automáticamente el filtro **"Generadas por IA"**.
4. Asegura que la imagen recién generada sea visible en el grid (scroll o highlight opcional).

---

## 8. Flujo de Navegación (Navigation Compose)

### Rutas sugeridas:

```kotlin
"feed"                         // Feed principal (Following / For You)
"item-detail/{publicationId}"  // Detalle de publicación
"profile"                      // Pantalla principal de perfil
"profile_model_pics/{filter}/{modelPicId}" // Perfil abierto en Model Pics con filtro y resultado opcional
```

### Secuencia de acciones:

1. Usuario está en **feed** o **item-detail**.
2. Pulsa **"Try On ✨"** en una publicación.
3. Se abre `TryOnDialog` con las imágenes base del usuario.
4. Selecciona una imagen (checkbox) → aparece botón "Try On ✨".
5. Pulsa "Try On ✨" → estado `generando` → llamada a Cloud Function → estado `listo` con imagen generada.
6. Pulsa "Continuar" → navegar a `profile_model_pics/generated/{modelPicId}`.
7. En `profile/model-pics`, se activa filtro "Generadas por IA" y se muestra la nueva imagen.

---

## 9. Plan de Implementación por Fases

### Fase 1: Modelo de datos y Firebase
- [x] Definir el modelo y guardar documentos en `users/{userId}/modelPics/{modelPicId}`.
- [x] Subir fotos base a Storage y registrar sus metadatos en Firestore desde una sola acción.
- [x] Configurar y probar la regla de Storage para `modelPics/{userId}/{fileName}`.
- [ ] Revisar que las reglas de Firestore limiten Model Pics al propietario; la regla catch-all actual permite acceso a cualquier usuario autenticado.

### Fase 2: Front-end del Try-On (en progreso)
- [x] Agregar botón compacto "Try On ✨" a prendas/outfits de Following y For You.
- [x] Agregar botón compacto "Try On ✨" a publicaciones de prenda/outfit en Item Detail.
- [x] Abrir el mismo `TryOnDialog` desde el feed y el detalle.
- [x] Cargar las fotos base del usuario y permitir selección única con checkbox.
- [x] Crear estados de UI para carga, resultado y error; implementar sus vistas en el diálogo.
- [x] Crear navegación al tab Model Pics con filtro base o generado.
- [x] Mostrar la carga y los errores reales de la llamada suspendible al Repository.
- [ ] Validar visualmente el flujo en dispositivo/emulador.

### Fase 3: Persistencia del resultado (preparación)
- [x] Definir campos para vincular el resultado con la foto base y la publicación de origen.
- [x] Definir el contrato de resultado directo entre repository, ViewModel y diálogo.
- [x] Al abrir Model Pics con el ID del resultado, activar el filtro y desplazar/resaltar esa imagen.
- [x] Conectar el botón con la llamada suspendible del Repository y retornar el resultado al diálogo.

### Fase 4: Cloud Function y FASHN (código implementado; falta configurar entorno y desplegar)
- [x] Crear la Callable protegida con Firebase Auth y respuesta directa al cliente.
- [x] Leer la API key del servidor mediante `process.env.FASHN_API_KEY`, siguiendo el patrón de OpenRouter.
- [x] Consultar el prediction ID asíncrono dentro de la función, con timeout y manejo de errores.
- [x] Usar `tryon-max` con un prompt distinto para prenda y outfit, y pedir una sola salida de 1 crédito.
- [x] Subir el resultado a Storage y guardar un documento `type: "generated"` en Firestore.
- [x] Conectar el Repository y `TryOnViewModel` a la respuesta callable directa; sin colección de jobs ni listener.
- [ ] Confirmar que `FASHN_API_KEY` está disponible en el entorno desplegado, desplegar Functions y probar el flujo real.

### Fase 5: Pruebas finales
- [x] Pruebas unitarias de selección, resultado del Repository y estados de `TryOnViewModel`.
- [ ] Pruebas de integración con Cloud Function y Storage.
- [ ] Pruebas en dispositivo real (diálogo, navegación, rotación, estados de red).
- [ ] Manejo de errores: sin conexión, FASHN falla, imagen no compatible y usuario sin fotos base.

---

## 10. Consideraciones de Seguridad y Costos

| Riesgo | Mitigación |
|--------|------------|
| API Key de FASHN expuesta | Mantener solo en Cloud Function, nunca en el cliente. |
| Usuario malintencionado genera miles de imágenes | Verificar Firebase Auth en la función; opcional: límite de generación por usuario/día. |
| Imágenes muy grandes consumen Storage | Limitar tamaño y resolución en Subida y en Cloud Function. |
| Acceso a imágenes de otros usuarios | Reglas de Firestore y Storage: `userId == request.auth.uid`. |
| Botón Try On visible para usuarios no autenticados | Ocultar botón o mostrar estado de "Inicia sesión para probar" si no hay `FirebaseAuth.currentUser`. |

---

## 11. Checklist de Entrega

- [x] Botón compacto "Try On ✨" visible solo en prendas/outfits del feed (Following y For You).
- [x] Botón compacto "Try On ✨" visible solo en prendas/outfits en Item Detail.
- [x] Tab "Model Pics" y filtros base/IA.
- [x] Subida de foto desde galería a Storage y metadatos en Firestore.
- [x] Popup de Try-On con fondo atenuado/borroso.
- [x] Selección de una foto base dentro del popup.
- [x] Botón "Try On ✨" habilitado tras seleccionar una foto.
- [x] Vistas de carga/resultado conectadas a los estados devueltos por el Repository.
- [x] Ruta preparada al tab Model Pics con filtro solicitado.
- [ ] Generar en Firebase real y comprobar que la imagen queda visible en el filtro IA.
- [x] API Key de FASHN no se incluye en el cliente; se lee del entorno del servidor.
- [ ] Reglas de Firestore y Storage configuradas.

---

## 12. Notas Adicionales

- **Nombre del tab:** Se sugiere mantener **"Model Pics"** por claridad, aunque se podría usar *"Mis Modelos"* o *"Fotos Base"*.
- **Origen de la imagen base:** El usuario debe haber subido previamente al menos una foto de cuerpo completo al tab Model Pics para poder usar Try On. Si no tiene ninguna, el popup puede mostrar un estado vacío con opción de subir una foto.
- **Popup vs. Pantalla nueva:** El requisito especifica popup dentro del mismo flujo. Usar `Dialog` de Compose evita agregar rutas de navegación innecesarias para la selección.
- **Publicación asociada:** Cada imagen generada guarda `sourcePublicationId` para rastrear de qué prenda/publicación se originó.

---

*Fin del plan. Listo para iniciar desarrollo en la fase que el equipo decida.*
