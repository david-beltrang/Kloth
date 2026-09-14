# Kloth

<p align="center">
  <img src="docs/logo/KlothLogoNegroTrsparente.png" alt="Kloth logo" width="180"/>
</p>

**Kloth** es una red social móvil enfocada en moda. Permite descubrir, crear, calificar y reseñar artículos del mundo fashion, e interactuar con la comunidad mediante comentarios, likes, seguidores y un feed de recomendaciones.

> Aplicación Android nativa · Kotlin · Jetpack Compose  
> Proyecto académico — Desarrollo Móvil

---

## ¿Qué es Kloth?

A diferencia de una red social tradicional basada solo en publicaciones, Kloth combina dos tipos de contenido bajo la entidad **Artículo**:

| Tipo | Categorías | Idea |
|------|------------|------|
| **Publicación** | Prendas, Outfits | Contenido personal del usuario; aparece en su perfil |
| **Catálogo** | Marcas, Eventos | Registro compartido y reutilizable por toda la comunidad |

Sobre cualquier artículo se pueden buscar, ver detalle, calificar (0–5) estrellas, reseñar, comentar y dar like.

---

## Funcionalidades previstas

- **Autenticación**: registro, login y eliminación de cuenta
- **Perfil**: foto, publicaciones propias y artículos calificados
- **Feed**: reseñas de usuarios seguidos y feed de recomendaciones (*For You*)
- **Explorar / catálogo**: listado de artículos, búsqueda y filtros por subcategoría
- **Detalle de artículo**: información, calificación y reseñas
- **Interacción social**: comentarios (2 niveles), likes, seguir / dejar de seguir
- **Creación de contenido**: prendas, outfits, marcas y eventos
- **Notificaciones**: seguidores, likes, comentarios y respuestas

Documentación completa de requisitos y reglas de negocio: [`docs/requirements.md`](docs/requirements.md) · [`docs/functional-spec.md`](docs/functional-spec.md)

---

## Estado actual

- Login, registro y recuperación de contraseña

- Feed con tabs de Following y For You

- Buscar y explorar artículos con filtros por categoría

- Detalle de artículo con rating y reseñas

- Crear prendas, outfits, marcas y eventos

- Comentarios con respuestas (2 niveles), likes y seguimiento

- Notificaciones de seguidores, likes, comentarios y menciones

- Perfil con stats, publicaciones y artículos calificados

La navegación entre pantallas está implementada y funciona. Los datos por ahora son mock — todavía no hay backend.

---

## Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** + Material 3 | UI declarativa |
| **Android Gradle Plugin** | Build del proyecto |
| **Coil** | Carga de imágenes |

---

## Estructura del repositorio

```text

Kloth/

├── docs/               # Specs, diagramas y logo

├── project/            # El proyecto Android (abrir esta en Android Studio)

│   └── app/src/main/java/com/example/kloth/

│       ├── data/       # Modelos mock

│       ├── ui/

│       │   ├── screens/    # Feed, Explore, Detail, Profile, Auth, etc.

│       │   ├── components/ # Componentes reutilizables

│       │   ├── navigation/ # Nav graph

│       │   └── theme/      # Colores y tipografía

│       └── ...

└── README.md

```

---

## Cómo ejecutar el proyecto

### Requisitos

- [Android Studio](https://developer.android.com/studio) (versión reciente recomendada)
- JDK 11 o superior
- Emulador Android o dispositivo físico (API 26+)

### Pasos

1. Clona el repositorio:

```bash
git clone https://github.com/david-beltrang/Kloth.git
cd Kloth
```

2. Abre la carpeta **`project/`** en Android Studio realiza (File → Open → selecciona `project`).
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la app en un emulador o dispositivo (`Run`).

Para previsualizar otra pantalla, comenta/descomenta el composable correspondiente en `MainActivity.kt`.

---

Este repositorio se distribuye bajo la licencia [MIT](https://opensource.org/licenses/MIT).
