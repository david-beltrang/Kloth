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

Sobre cualquier artículo se pueden buscar, ver detalle, calificar (0–5), reseñar, comentar y dar like.

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

La app está en fase de **UI / prototipo visual**. Las pantallas usan datos de ejemplo; la navegación entre pantallas está desactivada temporalmente (se alterna la pantalla activa desde `MainActivity`).

| Pantalla | Descripción |
|----------|-------------|
| **Feed** | Timeline con tabs (Following / For You), posts y bottom navigation |
| **Explore** | Catálogo con búsqueda, chips de categoría y filtros |
| **Item Detail** | Hero image, info del producto, ratings y reseñas |
| **Profile** | Header de usuario, stats y tabs de contenido |

---

## Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** + Material 3 | UI declarativa |
| **Android Gradle Plugin** | Build del proyecto |
| **Coil** | Carga de imágenes |
| **AndroidX Lifecycle / Activity Compose** | Ciclo de vida y host Compose |

- **minSdk**: 26  
- **targetSdk / compileSdk**: 37  
- **applicationId**: `com.example.kloth`

---

## Estructura del repositorio

```text
Kloth/
├── docs/                     # Documentación del producto y del equipo
│   ├── functional-spec.md    # Especificación funcional
│   ├── requirements.md       # Requisitos funcionales y no funcionales
│   ├── workflow.md           # Git workflow y Conventional Commits
│   ├── diagrams/             # Diagramas de dominio
│   └── logo/                 # Assets de marca
├── project/                  # Proyecto Android (abrir esta carpeta en Android Studio)
│   └── app/src/main/java/com/example/kloth/
│       ├── MainActivity.kt
│       ├── data/             # Modelos de datos (mock)
│       ├── navigation/       # Nav graph (pendiente de activar)
│       └── ui/
│           ├── components/   # Componentes reutilizables
│           ├── screens/      # Feed, Explore, Detail, Profile
│           └── theme/        # Colores, tipografía y tema
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

2. Abre la carpeta **`project/`** en Android Studio (File → Open → selecciona `project`).
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la app en un emulador o dispositivo (`Run`).

Para previsualizar otra pantalla, comenta/descomenta el composable correspondiente en `MainActivity.kt`.

---

## Flujo de trabajo (Git)

El equipo trabaja con ramas permanentes **`main`** (estable) y **`develop`** (integración), más ramas temporales `feature/`, `fix/`, `docs/`, etc.

```text
Issue → rama desde develop → commits → PR a develop → review → merge
                                      (al cerrar sprint: develop → main)
```

- Commits con [Conventional Commits](docs/workflow.md) (`feat`, `fix`, `docs`, …)
- Los **Pull Requests** van hacia **`develop`**, no hacia `main`
- Detalle completo: [`docs/workflow.md`](docs/workflow.md)

---

## Documentación

| Documento | Contenido |
|-----------|-----------|
| [Especificación funcional](docs/functional-spec.md) | Alcance, tipos de artículo, reglas de negocio |
| [Requisitos](docs/requirements.md) | RF / RNF, actores y pendientes con el cliente |
| [Git workflow](docs/workflow.md) | Ramas, commits y proceso de PR |
| [Issue / PR templates](.github/ISSUE_TEMPLATE/) | Plantillas de issues y pull requests |

---

## Contribución

1. Crea un Issue para la tarea.
2. Crea tu rama desde `develop` (`feature/<nombre>`, `fix/<nombre>`, …).
3. Implementa los cambios con commits convencionales.
4. Abre un Pull Request hacia **`develop`**.
5. Espera review y merge.

---

## Licencia

Este repositorio se distribuye bajo la licencia [MIT](https://opensource.org/licenses/MIT).
