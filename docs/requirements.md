
# 1. Actores y Usuarios

| Actor                                  | Descripción                                                                                                                                                                                                    | Origen                                                                                |
| -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| **Usuario no autenticado (Visitante)** | Persona que aún no tiene cuenta o no ha iniciado sesión. Solo puede acceder a las pantallas de registro e inicio de sesión.                                                                                    | Enunciado: *"ingresar únicamente a todas las funcionalidades si se está autenticado"* |
| **Usuario autenticado**                | Actor principal del sistema. Puede crear publicaciones y artículos de catálogo, calificar, reseñar, comentar, dar like, seguir/dejar de seguir, editar su perfil, eliminar su cuenta y recibir notificaciones. | Enunciado + Especificación funcional, secciones 5–17                                  |
| **Usuario administrador**              | Actor con permisos especiales para gestionar contenido de catálogo en casos específicos (p. ej. eliminación de artículos de catálogo con reseñas o comentarios asociados).                                     | Especificación funcional, secciones 5 y 16                                            |

> Pendiente de definición: el rol de administrador no fue solicitado por el cliente en el enunciado original. Falta validar con el cliente si aprueba este usuario, cuál sería su alcance exacto de permisos, y cómo se asignaría este rol a una cuenta.

> Pendiente de definición: no específica si existen distintos tipos de usuario autenticado (por ejemplo, cuentas verificadas o cuentas de marca), por lo que se asume un único tipo de usuario autenticado con las mismas capacidades para todos.


---

  
# 2. Requisitos Funcionales

## 2.1 Autenticación y cuenta

- **RF-01**: El sistema debe permitir crear una cuenta capturando correo electrónico, nombre de usuario y contraseña.
- **RF-02**: El sistema debe permitir iniciar sesión con las credenciales registradas.
- **RF-03**: El sistema debe restringir el acceso a todas las funcionalidades de la aplicación exclusivamente a usuarios autenticados.
- **RF-04**: El sistema debe permitir eliminar la cuenta del usuario autenticado.

  
> Pendiente de definición: el cliente mencionó la posibilidad de capturar datos adicionales de perfil (*"si quieres recopilar datos adicionales podría ser un plus, pero es importante que primero lo discutamos"*). La especificación funcional se propone biografía y galería de imágenes, pero esto no ha sido validado con el cliente.


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
- **RF-15**: El sistema debe requerir, junto con la calificación un texto de reseña; no debe ser posible enviar solo la calificación sin texto.
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

> Pendiente de definición: RF-30 depende de la validación del rol de administrador (ver sección 1). RF-31 a RF-33 corresponden a un mecanismo de validación de duplicados propuesto por el equipo, no solicitado por el cliente; no se especifica el algoritmo de similitud que determinaría una "coincidencia similar" para Marcas.

## 2.9 Eliminación de cuenta

- **RF-35**: Al eliminar su cuenta, el sistema debe eliminar las publicaciones del usuario, conservar los artículos de catálogo que haya creado, y mostrar sus reseñas conservadas con el autor identificado como "Usuario eliminado".

> Pendiente de definición: este comportamiento fue propuesto por el equipo y no ha sido validado explícitamente con el cliente.

## 2.10 Notificaciones

- **RF-36**: El sistema debe generar una notificación cuando el usuario obtenga un nuevo seguidor, reciba un comentario, reciba un like, o reciba una respuesta a un comentario.

> Pendiente de definición: No se especifica si las notificaciones deben ser push (sistema operativo), internas a la aplicación, o ambas.

## 2.11 Feed adicional

- **RF-37**: El sistema debe mostrar, además del feed principal de seguidos, un feed adicional de recomendaciones (for you page) que incluya contenido de usuarios no seguidos.

> Pendiente de definición: esta funcionalidad ("For You") fue propuesta por el equipo y no fue solicitada por el cliente, quien únicamente pidió el feed de seguidos.


---
# 3. Requisitos No Funcionales

- **RNF-01**: La aplicación debe desarrollarse para la plataforma Android.
- **RNF-02**: La aplicación no requiere ser responsive; se asume su uso inicial únicamente en teléfonos celulares, sin soporte para tablets.
- **RNF-03**: El código fuente debe estar adecuadamente comentado. 
- **RNF-04**: El código debe usar nombres de variables claros y significativos. 
- **RNF-05**: Los formularios de la aplicación deben contar con validación de datos de entrada.
- **RNF-07**: El proyecto debe organizarse en paquetes de manera coherente.
  
> Pendiente de definición: no se especifican requisitos de seguridad más allá de la autenticación (p. ej. cifrado de datos en tránsito o en reposo, políticas de privacidad).

> Pendiente de definición: no se especifica un requisito de disponibilidad, escalabilidad, ni número esperado de usuarios concurrentes.

---
# 4. Reglas de Negocio

- **RN-01**: La calificación de un artículo debe ser un número entero entre 0 y 5.
- **RN-02**: Un usuario puede tener como máximo una reseña activa por artículo.
- **RN-03**: Una reseña siempre debe tener calificación y texto en conjunto; no puede existir una calificación sin texto asociado.
- **RN-04**: El texto de una reseña no puede exceder los 300 caracteres.
- **RN-05**: El sistema de comentarios tiene únicamente dos niveles: comentario directo sobre una reseña (nivel 1) y respuesta a un comentario (nivel 2). No existen niveles adicionales de anidamiento.
- **RN-06**: Los perfiles de usuario son públicos por defecto; no existen perfiles privados.
- **RN-07**: Las publicaciones (Prendas, Outfits) se asocian y muestran en el perfil de su creador; los artículos de catálogo (Marcas, Eventos) nunca se muestran como publicaciones personales.
- **RN-08**: Un artículo de catálogo (Marca o Evento) debe mantenerse como un único registro reutilizable por toda la comunidad.
- **RN-09**: Solo existe un tipo de reacción: like.
  
> Pendiente de definición: la regla de que un usuario solo puede responder una vez a la misma reseña y una vez al mismo comentario no ha sido validada con el cliente (relacionada con RF-20/RF-21).

> Pendiente de definición: la regla de que un artículo de catálogo con reseñas o comentarios asociados no puede eliminarse por su creador (solo por administrador) depende del cliente.
  
---
