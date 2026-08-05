# Documento de Requisitos
## Red Social de Moda — Aplicación Móvil de Reseñas

> **Versión:** 1.0
>
> **Fuentes utilizadas:** (1) Enunciado del proyecto entregado por el docente/cliente ("Proyecto: Aplicación móvil para reseña de artículos"); (2) Especificación Funcional v1.1 ("Red Social de Moda").
>
> **Método:** Todo requisito, regla, flujo o restricción documentado a continuación se deriva directa o inequívocamente de las fuentes anteriores. Donde la información es insuficiente o no ha sido validada, se marca explícitamente con `> Pendiente de definición`. No se han agregado funcionalidades, actores, integraciones o reglas que no estén respaldadas por el contexto disponible.

---

# 1. Actores y Usuarios

| Actor | Descripción | Origen |
|---|---|---|
| **Usuario no autenticado (Visitante)** | Persona que aún no tiene cuenta o no ha iniciado sesión. Solo puede acceder a las pantallas de registro e inicio de sesión. | Enunciado: *"ingresar únicamente a todas las funcionalidades si se está autenticado"* |
| **Usuario autenticado** | Actor principal del sistema. Puede crear publicaciones y artículos de catálogo, calificar, reseñar, comentar, dar like, seguir/dejar de seguir, editar su perfil, eliminar su cuenta y recibir notificaciones. | Enunciado + Especificación funcional, secciones 5–17 |
| **Usuario administrador** | Actor con permisos especiales para gestionar contenido de catálogo en casos específicos (p. ej. eliminación de artículos de catálogo con reseñas o comentarios asociados). | Especificación funcional, secciones 5 y 16 |

> Pendiente de definición: el rol de administrador no fue solicitado por el cliente en el enunciado original. Falta validar con el cliente si aprueba esta figura, cuál sería su alcance exacto de permisos, y cómo se asignaría este rol a una cuenta.

> Pendiente de definición: el enunciado no específica si existen distintos tipos de usuario autenticado (por ejemplo, cuentas verificadas o cuentas de marca), por lo que se asume un único tipo de usuario autenticado con las mismas capacidades para todos.

---

# 2. Requisitos Funcionales

## 2.1 Autenticación y cuenta

- **RF-01**: El sistema debe permitir crear una cuenta capturando correo electrónico, nombre de usuario y contraseña.
- **RF-02**: El sistema debe permitir iniciar sesión con las credenciales registradas.
- **RF-03**: El sistema debe restringir el acceso a todas las funcionalidades de la aplicación exclusivamente a usuarios autenticados.
- **RF-04**: El sistema debe permitir eliminar la cuenta del usuario autenticado.

> Pendiente de definición: el cliente mencionó la posibilidad de capturar datos adicionales de perfil (*"si quieres recopilar datos adicionales podría ser un plus, pero es importante que primero lo discutamos"*). La especificación funcional propone biografía y galería de imágenes, pero esto no ha sido validado con el cliente.

> Pendiente de definición: no se especifican requisitos de seguridad de la contraseña (longitud mínima, complejidad), ni el flujo de recuperación de contraseña olvidada, ni un proceso de verificación de correo electrónico.

## 2.2 Perfil

- **RF-05**: El sistema debe permitir modificar el perfil del usuario, incluyendo la carga de una foto de perfil.
- **RF-06**: El sistema debe mostrar en el perfil del usuario las publicaciones (Prendas, Outfits) que ha creado.
- **RF-07**: El sistema debe permitir al usuario visualizar un listado de todos los artículos que ha calificado.

> Pendiente de definición: los artículos de catálogo (Marcas, Eventos) no se muestran como publicaciones en el perfil de su creador (Especificación funcional, sección 3.2); no se especifica si el perfil debe mostrar, en alguna sección separada, los artículos de catálogo que el usuario ha creado.

## 2.3 Descubrimiento, navegación y búsqueda

- **RF-08**: El sistema debe mostrar una pantalla principal (feed) con las reseñas/análisis de los usuarios que se sigue, priorizando las más recientes.
- **RF-09**: El sistema debe mostrar una pantalla con los últimos artículos disponibles.
- **RF-10**: El sistema debe permitir filtrar los artículos mostrados por subcategoría.
- **RF-11**: El sistema debe proveer una barra de búsqueda para encontrar artículos por nombre.
- **RF-12**: El sistema debe permitir aplicar filtros adicionales específicos según la categoría del artículo:
  - Prendas: marca, color, categoría, precio.
  - Marcas: país, tipo.
  - Eventos: país, ciudad, año.
  - Outfits: estilo.

## 2.4 Detalle de artículo, calificación y reseñas

- **RF-13**: El sistema debe permitir visualizar el detalle de un artículo, incluyendo sus datos principales y las reseñas existentes de otros usuarios.
- **RF-14**: El sistema debe permitir a un usuario autenticado calificar un artículo con un número entero entre 0 y 5.
- **RF-15**: El sistema debe requerir, junto con la calificación, un texto de reseña; no debe ser posible enviar solo la calificación sin texto.
- **RF-16**: El sistema debe limitar el texto de la reseña a un máximo de 300 caracteres.
- **RF-17**: El sistema debe permitir a un usuario realizar como máximo una reseña por artículo.
- **RF-18**: El sistema debe permitir a un usuario editar la calificación y/o el texto de una reseña que haya creado previamente.
- **RF-19**: El sistema debe permitir a un usuario eliminar una reseña que haya creado.

## 2.5 Comentarios

- **RF-20**: El sistema debe permitir a cualquier usuario autenticado comentar la reseña de otro usuario, sin necesidad de seguirlo.
- **RF-21**: El sistema debe permitir responder a un comentario existente (comentario de segundo nivel), sin permitir niveles adicionales de anidamiento.
- **RF-22**: El sistema debe permitir eliminar comentarios propios. Los comentarios no pueden editarse.

> Pendiente de definición: la restricción de que un usuario solo pueda responder una vez a la misma reseña y una vez al mismo comentario fue propuesta por el equipo de desarrollo y no ha sido validada con el cliente.

## 2.6 Likes

- **RF-23**: El sistema debe permitir a un usuario dar "like" a una reseña.
- **RF-24**: El sistema debe permitir a un usuario dar "like" a un comentario.

> Pendiente de definición: el cliente dejó explícitamente abierta esta funcionalidad (*"¿Sería muy complicado? Bueno después lo podemos discutir"*). El alcance final (si aplica solo a reseñas, o también a comentarios, y si existe un único tipo de reacción) no ha sido cerrado con el cliente.

## 2.7 Seguidores

- **RF-25**: El sistema debe permitir a un usuario seguir a otro usuario.
- **RF-26**: El sistema debe permitir a un usuario dejar de seguir a otro usuario.

## 2.8 Creación y gestión de artículos

- **RF-27**: El sistema debe permitir a cualquier usuario crear una publicación de tipo Prenda u Outfit.
- **RF-28**: El sistema debe permitir a cualquier usuario crear un artículo de catálogo de tipo Marca o Evento.
- **RF-29**: El sistema debe permitir al creador de una publicación eliminarla en cualquier momento.
- **RF-30**: El sistema debe permitir eliminar un artículo de catálogo únicamente si no posee reseñas ni comentarios asociados; en caso contrario, solo el usuario administrador podrá eliminarlo.
- **RF-31**: El sistema debe validar, antes de crear una Marca, la normalización del texto (minúsculas, eliminación de espacios innecesarios) y verificar si ya existe una marca equivalente, sugiriendo coincidencias si las encuentra.
- **RF-32**: El sistema debe validar, antes de crear un Evento, el nombre normalizado y el año; si existe un evento equivalente, no debe permitir su creación.
- **RF-33**: El sistema debe validar, antes de crear una Prenda, la coincidencia exacta del nombre, permitiendo variantes del mismo producto.
- **RF-34**: El sistema no debe aplicar validación de duplicados para Outfits.

> Pendiente de definición: RF-30 depende de la validación del rol de administrador (ver sección 1). RF-31 a RF-33 corresponden a un mecanismo de validación de duplicados propuesto por el equipo, no solicitado por el cliente; no se especifica el algoritmo o umbral de similitud que determinaría una "coincidencia similar" para Marcas.

## 2.9 Eliminación de cuenta

- **RF-35**: Al eliminar su cuenta, el sistema debe eliminar las publicaciones del usuario, conservar los artículos de catálogo que haya creado, y mostrar sus reseñas conservadas con el autor identificado como "Usuario eliminado".

> Pendiente de definición: este comportamiento fue propuesto por el equipo y no ha sido validado explícitamente con el cliente.

## 2.10 Notificaciones

- **RF-36**: El sistema debe generar una notificación cuando el usuario obtenga un nuevo seguidor, reciba un comentario, reciba un like, o reciba una respuesta a un comentario.

> Pendiente de definición: no se especifica si las notificaciones deben ser push (sistema operativo), internas a la aplicación, o ambas.

## 2.11 Feed adicional

- **RF-37**: El sistema debe mostrar, además del feed principal de seguidos, un feed adicional de recomendaciones que incluya contenido de usuarios no seguidos.

> Pendiente de definición: esta funcionalidad ("For You") fue propuesta por el equipo y no fue solicitada por el cliente, quien únicamente pidió el feed de seguidos.

---

# 3. Requisitos No Funcionales

- **RNF-01**: La aplicación debe desarrollarse para la plataforma Android. *(Enunciado)*
- **RNF-02**: La aplicación no requiere ser responsive; se asume su uso inicial únicamente en teléfonos celulares, sin soporte para tablets. *(Enunciado: "no importante que la aplicación sea responsive, podemos suponer que inicialmente solo se va a usar en celulares y nada de tablets")*
- **RNF-03**: El código fuente debe estar adecuadamente comentado. *(Derivado del criterio de evaluación del product owner: penalización por "código no comentado")*
- **RNF-04**: El código debe usar nombres de variables claros y significativos. *(Derivado de: "malos nombres de variables")*
- **RNF-05**: Los formularios de la aplicación deben contar con validación de datos de entrada. *(Derivado de: "no hay validación de los formularios")*
- **RNF-06**: El código no debe presentar duplicación, ya sea en funciones o en componentes. *(Derivado de: "código duplicado")*
- **RNF-07**: El proyecto debe organizarse en paquetes de manera coherente. *(Derivado de: "usar paquetes para organizar el proyecto de manera coherente")*

> Pendiente de definición: la penalización por "problemas de rendimiento de la aplicación" indica que el rendimiento es un criterio de evaluación, pero no se especifican métricas concretas (tiempos de respuesta, uso de memoria, tamaño de la aplicación, etc.).

> Pendiente de definición: no se especifican requisitos de seguridad más allá de la autenticación (p. ej. cifrado de datos en tránsito o en reposo, políticas de privacidad).

> Pendiente de definición: no se especifica un requisito de disponibilidad, escalabilidad, ni número esperado de usuarios concurrentes.

---

# 4. Reglas de Negocio

- **RN-01**: La calificación de un artículo debe ser un número entero entre 0 y 5 (inclusive).
- **RN-02**: Un usuario puede tener como máximo una reseña activa por artículo.
- **RN-03**: Una reseña siempre debe tener calificación y texto en conjunto; no puede existir una calificación sin texto asociado.
- **RN-04**: El texto de una reseña no puede exceder los 300 caracteres.
- **RN-05**: El sistema de comentarios tiene únicamente dos niveles: comentario directo sobre una reseña (nivel 1) y respuesta a un comentario (nivel 2). No existen niveles adicionales de anidamiento.
- **RN-06**: Los perfiles de usuario son públicos por defecto; no existen perfiles privados.
- **RN-07**: Las publicaciones (Prendas, Outfits) se asocian y muestran en el perfil de su creador; los artículos de catálogo (Marcas, Eventos) nunca se muestran como publicaciones personales.
- **RN-08**: Un artículo de catálogo (Marca o Evento) debe mantenerse como un único registro reutilizable por toda la comunidad.
- **RN-09**: Solo existe un tipo de reacción: like.

> Pendiente de definición: RN-09 refleja la propuesta actual de la especificación funcional, pero el cliente dejó este punto abierto explícitamente para discusión posterior.

> Pendiente de definición: la regla de que un usuario solo puede responder una vez a la misma reseña y una vez al mismo comentario no ha sido validada con el cliente (relacionada con RF-20/RF-21).

> Pendiente de definición: la regla de que un artículo de catálogo con reseñas o comentarios asociados no puede eliminarse por su creador (solo por administrador) depende de la validación del rol de administrador.

> Pendiente de definición: la regla de conservación de artículos de catálogo y anonimización de reseñas ("Usuario eliminado") tras eliminar una cuenta no ha sido validada con el cliente.

---

# 5. Flujos Principales

**F1 — Registro de usuario**
1. El usuario no autenticado accede a la pantalla de registro.
2. Ingresa correo electrónico, nombre de usuario y contraseña.
3. El sistema crea la cuenta y autentica al usuario.

**F2 — Inicio de sesión**
1. El usuario no autenticado accede a la pantalla de inicio de sesión.
2. Ingresa sus credenciales.
3. El sistema valida las credenciales y concede acceso a la aplicación.

**F3 — Consultar el feed principal**
1. El usuario autenticado abre la aplicación.
2. El sistema muestra las reseñas de los usuarios que sigue, ordenadas de la más reciente a la más antigua.

**F4 — Descubrir artículos por categoría**
1. El usuario navega a la pantalla de artículos.
2. Selecciona una categoría (Prendas, Outfits, Marcas, Eventos).
3. Aplica, opcionalmente, filtros específicos de la categoría.
4. El sistema muestra los artículos que cumplen los criterios.

**F5 — Buscar un artículo**
1. El usuario ingresa un término en la barra de búsqueda.
2. El sistema retorna los artículos coincidentes por nombre.

**F6 — Calificar y reseñar un artículo**
1. El usuario selecciona un artículo y accede a su detalle.
2. Ingresa una calificación (0–5) y un texto de reseña (máx. 300 caracteres).
3. El sistema valida que el usuario no tenga ya una reseña para ese artículo.
4. El sistema guarda la reseña.

**F7 — Editar una reseña propia**
1. El usuario accede a una reseña que ha creado.
2. Modifica la calificación y/o el texto.
3. El sistema actualiza la reseña existente.

**F8 — Comentar una reseña**
1. El usuario visualiza una reseña.
2. Escribe un comentario.
3. El sistema guarda el comentario y notifica al autor de la reseña.

**F9 — Responder a un comentario**
1. El usuario visualiza un comentario existente.
2. El sistema verifica que el usuario no haya respondido previamente a ese comentario.
3. El usuario escribe su respuesta.
4. El sistema guarda la respuesta y notifica al autor del comentario original.

**F10 — Dar like a una reseña o comentario**
1. El usuario selecciona la opción de like sobre una reseña o comentario.
2. El sistema registra el like y notifica al autor del contenido.

**F11 — Seguir o dejar de seguir a un usuario**
1. El usuario visita el perfil de otro usuario.
2. Selecciona la opción de seguir o dejar de seguir.
3. El sistema actualiza la relación y, si aplica, notifica al usuario seguido.

**F12 — Crear un artículo de catálogo (Marca o Evento)**
1. El usuario accede al formulario de creación correspondiente.
2. Ingresa los datos requeridos.
3. El sistema ejecuta la validación de duplicados de la categoría.
4. Si no hay duplicado, se crea el artículo; si lo hay, se muestra una sugerencia (Marcas) o se bloquea la creación (Eventos).

**F13 — Crear una publicación (Prenda u Outfit)**
1. El usuario accede al formulario de creación.
2. Ingresa los datos (para Prendas, el sistema valida coincidencia exacta de nombre).
3. El sistema crea la publicación y la asocia al perfil del usuario.

**F14 — Editar perfil**
1. El usuario accede a la pantalla de edición de perfil.
2. Modifica los datos permitidos, incluyendo la foto de perfil.
3. El sistema guarda los cambios.

**F15 — Eliminar cuenta**
1. El usuario selecciona la opción de eliminar cuenta.
2. El sistema elimina al usuario y sus publicaciones, conserva sus artículos de catálogo y anonimiza sus reseñas como "Usuario eliminado".

> Pendiente de definición: no se especifica si el flujo de eliminación de cuenta (F15) requiere un paso de confirmación explícita o reautenticación antes de ejecutarse.

**F16 — Recibir notificaciones**
1. Ocurre un evento relevante (nuevo seguidor, comentario, like, respuesta).
2. El sistema genera una notificación para el usuario correspondiente.

> Pendiente de definición: no se especifica el medio de entrega de las notificaciones (push del sistema operativo, in-app, o ambos) ni si el usuario puede configurar sus preferencias de notificación.

---

# 6. Restricciones y Dependencias

## 6.1 Plataforma y tecnología (según enunciado del docente/cliente)

- **RD-01**: La aplicación debe desarrollarse en Android (primera y segunda entrega).
- **RD-02**: En la tercera entrega debe incorporarse otro framework (Swift o Flutter) para al menos 2 pantallas adicionales y consumo de datos.
- **RD-03**: El backend debe incluir una API REST y una base de datos SQL.
- **RD-04**: El consumo de la API REST debe realizarse mediante Retrofit.
- **RD-05**: Debe existir una base de datos NoSQL en Firebase, con consumo de datos vía Firestore.
- **RD-06**: La autenticación de usuarios debe implementarse mediante Firebase.
- **RD-07**: El manejo y consumo de recursos (imágenes) debe realizarse a través de Firebase/Firestore.
- **RD-08**: Las notificaciones (tercera entrega) deben implementarse a través de Firebase.
- **RD-09**: La arquitectura de la aplicación debe seguir el patrón MVVM.
- **RD-10**: El proyecto debe aplicar patrones de software adicionales: capas, repositorio, fachada, inyección de dependencias.
- **RD-11**: El proyecto debe hacer uso de Intents y manejo de permisos de Android.
- **RD-12**: La tercera entrega debe incluir pruebas automatizadas de la aplicación.

## 6.2 Gestión y entregables del proyecto

- **RD-13**: El proyecto debe mantenerse en un repositorio de GitHub.
- **RD-14**: Debe definirse, como parte de la primera entrega, el nombre de la aplicación, ícono, paleta de colores y tipografía.
- **RD-15**: El desarrollo debe seguir una metodología ágil basada en Scrum, con sprints semanales y avances funcionales obligatorios en cada entrega.
- **RD-16**: La tercera entrega debe incluir un video promocional de 5 minutos y una presentación promocional de máximo 10 minutos.

> Pendiente de definición: el enunciado menciona temas opcionales adicionales para la tercera entrega (IA en dispositivos móviles, accesibilidad, responsive, Kotlin multiplataforma, manejo de mapas, entre otros), condicionados al avance del curso. No se ha determinado cuáles de estos temas, si alguno, se incorporarán al proyecto.

## 6.3 Dependencias internas entre funcionalidades

- **RD-17**: Los siguientes puntos dependen de validación previa con el cliente antes de considerarse definitivos, y condicionan el diseño de los demás requisitos relacionados:
  - Existencia y alcance del rol de administrador (afecta RF-30, RN-relacionadas de eliminación de catálogo).
  - Alcance final del sistema de likes (afecta RF-23/RF-24).
  - Restricciones del sistema de comentarios de una respuesta por usuario (afecta RF-20/RF-21).
  - Mecanismo y umbral de validación de duplicados para Marcas y Eventos (afecta RF-31/RF-32).
  - Comportamiento de eliminación de cuenta respecto al catálogo y a las reseñas conservadas (afecta RF-35).
  - Inclusión del feed adicional "For You" (afecta RF-37).
  - Captura de datos adicionales de perfil (biografía, galería) (afecta RF-05 y sección 2.1).

---

*Fin del documento. Todos los puntos marcados como "Pendiente de definición" deben resolverse con el cliente antes de congelar el alcance funcional para el diseño de la base de datos, los diagramas de clases y los mockups.*
