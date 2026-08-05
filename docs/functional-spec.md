# Especificación Funcional del Proyecto
## Red Social de Moda

> **Versión:** 1.1
>
> **Estado:** Definición Funcional
>
> **Propósito:** Este documento define el alcance funcional de la aplicación móvil, las reglas de negocio y el comportamiento esperado del sistema. Su objetivo es servir como fuente principal para el diseño de la arquitectura, base de datos, diagramas UML, mockups y desarrollo del proyecto.

---

# 1. Descripción General

La aplicación consiste en una red social enfocada exclusivamente en el mundo de la moda. Su propósito es permitir que cualquier usuario descubra, cree, califique y reseñe artículos relacionados con este dominio, fomentando la interacción entre los miembros de la comunidad mediante comentarios, likes, seguidores y un sistema de recomendaciones.

A diferencia de una red social tradicional donde únicamente existen publicaciones, esta aplicación combina dos conceptos fundamentales:

- Publicaciones creadas por la comunidad.
- Artículos de catálogo compartidos por todos los usuarios.

Ambos tipos de contenido conforman la entidad principal denominada **Artículo**, sobre la cual se construyen todas las funcionalidades sociales de la aplicación.

---

# 2. Concepto Principal

Toda la aplicación gira alrededor de la entidad **Artículo**.

Un artículo representa cualquier elemento relacionado con el mundo de la moda que pueda ser descubierto, reseñado, calificado y comentado por la comunidad.

Todos los artículos, independientemente de su categoría, poseen las siguientes funcionalidades:

- Ser buscados.
- Ser visualizados.
- Recibir reseñas.
- Recibir una calificación.
- Recibir comentarios.
- Recibir likes.
- Aparecer dentro de las categorías correspondientes.
- Generar notificaciones relacionadas.

Aunque todos comparten el mismo comportamiento, existen dos grandes tipos de artículos.

---

# 3. Tipos de Artículo

## 3.1 Publicación

Las publicaciones representan contenido generado por la comunidad.

Características:

- Son creadas por cualquier usuario.
- Se muestran en el perfil del usuario que las creó.
- Poseen contenido multimedia.
- Representan experiencias personales del usuario.

Las publicaciones se dividen en dos categorías:

- Prendas
- Outfits

---

## 3.2 Catálogo

Los artículos de catálogo representan información compartida por toda la comunidad.

Su objetivo no es representar publicaciones personales sino construir un catálogo colaborativo.

Características:

- Pueden ser creados por cualquier usuario.
- No aparecen como publicaciones dentro del perfil del creador.
- Son reutilizados por toda la comunidad.
- La intención es que exista un único registro para cada elemento.

Las categorías de catálogo son:

- Marcas
- Eventos

---

# 4. Categorías

## 4.1 Prendas

Representan productos específicos relacionados con la moda.

Ejemplos:

- Nike Air Force 1
- Adidas Samba
- Chaqueta Levi's
- Hoodie Essentials

Cada prenda posee:

- Nombre
- Marca
- Categoría
- Color
- Imagen
- Descripción
- Precio

---

## 4.2 Outfits

Representan una publicación realizada por un usuario mostrando la combinación de prendas utilizada.

Cada outfit contiene:

- Una imagen
- Descripción
- Etiqueta de estilo
- Usuario creador

Ejemplos de etiquetas:

- Streetwear
- Business
- Casual
- Formal
- Deportivo
- Otaku
- Vintage

---

## 4.3 Marcas

Representan empresas relacionadas con la industria de la moda.

Pueden corresponder a:

- Ropa
- Calzado
- Perfumes
- Accesorios
- Moda en general

Cada marca contiene:

- Nombre
- Logo
- Página web
- País de origen
- Año de fundación
- Descripción

---

## 4.4 Eventos

Representan acontecimientos importantes del mundo de la moda.

Ejemplos:

- Paris Fashion Week
- Colombiamoda
- Milan Fashion Week

Cada evento contiene:

- Nombre
- Imagen
- Ciudad
- País
- Fecha de inicio
- Fecha de finalización
- Organizador
- Descripción

---

# 5. Usuarios

Todo usuario autenticado puede:

- Crear publicaciones.
- Crear artículos de catálogo.
- Crear reseñas.
- Comentar.
- Dar likes.
- Seguir usuarios.
- Modificar su perfil.
- Eliminar su cuenta.

Existe además un usuario administrador con permisos especiales para administrar contenido cuando sea necesario.

> **Pendiente de validar con el cliente:** alcance exacto del rol de administrador (ver sección 20, punto 1).

---

# 6. Perfil

Cada usuario posee un perfil compuesto por:

- Foto
- Nombre
- Nombre de usuario
- Biografía
- Número de seguidores
- Número de seguidos
- Cantidad de reseñas realizadas
- Cantidad de publicaciones creadas
- Promedio de calificaciones recibidas en sus publicaciones
- Galería de imágenes de sus publicaciones
- **Listado de artículos calificados por el usuario**, para que pueda tener seguimiento de lo que más le ha gustado (requisito explícito del cliente).

Únicamente las **Publicaciones** aparecen en el perfil como contenido creado.

Los artículos de **Catálogo** nunca aparecen como publicaciones personales.

---

# 7. Sistema de Reseñas

Cada usuario puede realizar únicamente una reseña por artículo.

La reseña siempre está compuesta por:

- Calificación
- Texto

No es posible realizar únicamente una calificación.

La calificación utiliza números enteros entre:

0 y 5.

Las reseñas poseen una longitud máxima de:

300 caracteres.

Las reseñas pueden eliminarse.

**Las reseñas pueden editarse.** El usuario puede modificar tanto la calificación como el texto de una reseña ya creada, sin necesidad de eliminarla y volver a crearla (requisito explícito del cliente).

---

# 8. Sistema de Comentarios

Cada reseña permite comentarios.

El sistema soporta únicamente dos niveles.

Nivel 1

Comentario directamente sobre la reseña.

Nivel 2

Respuesta a otro comentario.

Restricciones:

- Cada usuario únicamente puede responder una vez a una misma reseña.
- Cada usuario únicamente puede responder una vez a un comentario.

Los comentarios:

- Pueden eliminarse.
- No pueden editarse.

> **Pendiente de validar con el cliente:** la restricción de una sola respuesta por usuario (ver sección 20, punto 4).

---

# 9. Sistema de Likes

Los usuarios pueden indicar que les gusta:

- Una reseña.
- Un comentario.

Solo existe un tipo de reacción:

Like.

> **Pendiente de validar con el cliente:** si un solo tipo de reacción es suficiente (ver sección 20, punto 9).

---

# 10. Seguidores

Los perfiles son públicos por defecto.

Los usuarios pueden:

- Seguir otros usuarios.
- Dejar de seguirlos.

No existen perfiles privados.

---

# 11. Feed

La aplicación posee dos feeds principales.

## Following

Muestra publicaciones realizadas por los usuarios seguidos.

Ordenadas cronológicamente.

---

## For You

Muestra recomendaciones personalizadas.

Incluye contenido de usuarios no seguidos con el objetivo de descubrir nuevas publicaciones.

> **Pendiente de validar con el cliente:** este feed es una funcionalidad adicional propuesta, no solicitada originalmente (ver sección 20, punto 2).

---

# 12. Navegación

El usuario podrá navegar por categorías.

Cada categoría tendrá filtros específicos.

Ejemplos:

Prendas

- Marca
- Color
- Categoría
- Precio

Marcas

- País
- Tipo

Eventos

- País
- Ciudad
- Año

Outfits

- Estilo

---

# 13. Búsqueda

Todos los artículos pueden encontrarse mediante el buscador.

Dependiendo de la categoría podrán existir filtros adicionales.

---

# 14. Notificaciones

El sistema notificará cuando ocurra alguno de los siguientes eventos:

- Nuevo seguidor.
- Nuevo comentario.
- Like recibido.
- Respuesta a un comentario.

---

# 15. Validación de Duplicados

## Marcas

Antes de crear una marca el sistema:

- Normaliza el texto.
- Convierte a minúsculas.
- Elimina espacios innecesarios.

Posteriormente verifica si ya existe una marca equivalente.

Si encuentra coincidencias similares mostrará sugerencias al usuario.

---

## Eventos

El sistema verifica:

- Nombre normalizado.
- Año.

Si existe un evento equivalente no permitirá crear uno nuevo.

---

## Prendas

Se realiza únicamente una validación básica por coincidencia exacta del nombre.

Se permiten variantes del mismo producto.

---

## Outfits

No existe validación de duplicados.

Cada outfit representa una publicación única realizada por un usuario.

> **Pendiente de validar con el cliente:** todo este mecanismo de validación de duplicados es una funcionalidad adicional (ver sección 20, punto 7).

---

# 16. Eliminación de Artículos

## Publicaciones

El creador puede eliminarlas en cualquier momento.

---

## Catálogo

Solo pueden eliminarse si:

- No poseen reseñas.
- No poseen comentarios.

En caso contrario únicamente el administrador podrá eliminarlos.

> **Pendiente de validar con el cliente:** esta regla depende del rol de administrador (ver sección 20, puntos 1 y 5).

---

# 17. Eliminación de Cuenta

Cuando un usuario elimina su cuenta:

- El usuario desaparece.
- Sus publicaciones desaparecen.
- Sus artículos de catálogo permanecen.
- Las reseñas realizadas permanecen mostrando como autor "Usuario eliminado".

Con ello se preserva la integridad del catálogo colaborativo.

> **Pendiente de validar con el cliente:** este comportamiento no fue solicitado explícitamente (ver sección 20, punto 6).

---

# 18. Multimedia

Inicialmente todas las publicaciones utilizarán únicamente imágenes.

La arquitectura deberá permitir extender fácilmente el modelo para soportar videos en futuras versiones.

---

# 19. Filosofía del Producto

La aplicación busca combinar el concepto de una red social moderna con un catálogo colaborativo de artículos relacionados con la moda.

Las publicaciones permiten a los usuarios compartir su estilo personal mediante prendas y outfits, mientras que el catálogo proporciona información reutilizable sobre marcas y eventos del mundo de la moda.

Todo el contenido converge en un único modelo de interacción basado en reseñas, calificaciones, comentarios y likes, permitiendo una experiencia consistente independientemente del tipo de artículo consultado.

---

# 20. Funcionalidades y Decisiones Pendientes de Validar con el Cliente

Las siguientes funcionalidades o reglas de negocio son extensiones o interpretaciones del equipo de desarrollo que no fueron solicitadas explícitamente por el cliente en el enunciado original. Deben discutirse y aprobarse antes de considerarse requisitos definitivos.

1. **Rol de administrador** — el cliente no mencionó ningún usuario con permisos especiales. Definir si lo acepta y qué alcance tendría (¿puede borrar cualquier contenido?, ¿banear usuarios?, ¿quién ejercería este rol?).

2. **Feed "For You" (recomendaciones)** — el cliente solo pidió un feed de seguidos. Confirmar si desea esta segunda pestaña de descubrimiento o prefiere mantener un único feed.

3. **Modelo de dos tipos de artículo (Publicación vs. Catálogo)** — el cliente hablaba de `<Artículo>` como concepto único. Confirmar que aprueba la distinción entre contenido personal (Prendas/Outfits) y contenido colaborativo compartido (Marcas/Eventos).

4. **Comentarios de dos niveles con restricción de una respuesta por usuario** — el cliente solo pidió un sistema de comentarios, sin especificar niveles ni restricciones. Validar si esta limitación tiene sentido para la experiencia de usuario que imagina.

5. **Reglas de eliminación de artículos de catálogo** — que solo se puedan borrar si no tienen reseñas/comentarios y que, en caso contrario, solo el administrador pueda hacerlo. Confirmar si está de acuerdo con que un artículo de catálogo con interacciones no pueda eliminarse por su creador.

6. **Eliminación de cuenta y persistencia del catálogo** — confirmar que el cliente esté de acuerdo con que las publicaciones desaparezcan pero los artículos de catálogo creados por el usuario permanezcan, y que sus reseñas se muestren con autor "Usuario eliminado".

7. **Validación de duplicados diferenciada por categoría** — normalización de texto para marcas, nombre + año para eventos, coincidencia exacta para prendas, ninguna para outfits. Mostrar el problema (ej. "Nike" vs. "nike" vs. "NIKE") y confirmar el nivel de rigurosidad esperado.

8. **Datos adicionales de perfil (biografía, galería de imágenes)** — el cliente solo pidió foto de perfil como dato adicional. Confirmar si también desea biografía y galería.

9. **Sistema de likes limitado a un solo tipo de reacción** — el cliente dejó abierta la pregunta sobre el mecanismo de likes. Confirmar si un solo tipo de reacción (like) es suficiente o si espera algo más (ej. reacciones tipo emoji).

10. **Multimedia solo con imágenes, extensible a video** — no fue pedido explícitamente ni contradicho. Confirmarlo como alcance acordado para la primera versión.
