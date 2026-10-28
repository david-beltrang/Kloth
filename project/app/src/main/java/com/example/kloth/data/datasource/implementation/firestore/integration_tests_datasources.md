# 📌 Plan de Tests de Integración para Firestore

Este documento detalla los casos de prueba prioritarios para los orígenes de datos (Data Sources) de la aplicación, asegurando el correcto funcionamiento de las transacciones, subcolecciones y operaciones atómicas en Firebase Firestore.

---

## 🧥 1. Article Datasource (`ArticleFirestoreDataSourceImpl`)

Los tests más representativos deben cubrir el ciclo de vida básico y la lógica de limpieza:

### 1. `testCreateAndGetAllArticles` (Creación y Listado)
* **Qué prueba:** Que al llamar a `createArticle()`, el documento se guarda correctamente en la colección `"articles"`, y que `getAllArticles()` es capaz de leerlo, mapearlo y asignarle el ID del documento (`articleId = doc.id`).

### 2. `testGetArticleById` (Lectura por ID)
* **Qué prueba:** Que se puede recuperar un artículo específico mediante su ID y que lanza una excepción adecuada (`Exception("Artículo no encontrado")`) si el ID no existe en Firestore.

### 3. `testDeleteArticleWithCascade` (Eliminación con cascada simulada)
* **Qué prueba:** Como Firestore no tiene `ON DELETE CASCADE` nativo, este método en el datasource elimina primero las reseñas asociadas en la colección `"reviews"` y luego el artículo. Es vital probar que esta limpieza compuesta funcione correctamente.

---

## 💬 2. Review Datasource (`ReviewFirestoreDataSourceImpl`)

Las reseñas en la app manejan subcolecciones y lógica transaccional compleja, por lo que estos tres tests son de alta prioridad:

### 1. `testCreateAndGetReview` (Creación y Lectura de Reseña)
* **Qué prueba:** Operación básica de persistencia y recuperación de una reseña ligada a un artículo.

### 2. `testCreateCommentWithTransaction` (Creación de comentario con transacción)
* **Qué prueba:** Valida que `createComment()` ejecuta correctamente una transacción en Firestore: añade el documento del comentario en la subcolección `comments` y actualiza atómicamente el contador `commentsCount` en el documento de la reseña padre.

### 3. `testToggleReviewLike` (Gestión de Likes atómicos)
* **Qué prueba:** Prueba la lógica condicional y transaccional de dar/quitar like: si no existe el like lo crea y aumenta el contador `likesCount` con `FieldValue.increment(1)`, y si ya existe lo borra y decrementa el contador.

---

## 👤 3. User Datasource (`UserFirestoreDataSourceImpl`)

Para el módulo de usuarios, los tests de integración más valiosos son el registro, la actualización parcial y el sistema de seguidores:

### 1. `testRegisterAndGetUserById` (Registro y obtención de perfil)
* **Qué prueba:** Que `registerUser()` crea correctamente el documento del usuario usando el `userId` (proporcionado por Firebase Auth) como el ID exacto del documento en la colección `"users"`, y que `getUserById()` lo recupera íntegramente.

### 2. `testUpdateProfileWithMerge` (Actualización de perfil con merge y limpieza de campos)
* **Qué prueba:** Valida que `updateProfile()` actualiza solo los campos editables del perfil, aplica `SetOptions.merge()` y utiliza inteligentemente `FieldValue.delete()` si el usuario deja un campo opcional (como biografía o web) vacío.

### 3. `testFollowOrUnfollowUser` (Transacción bidireccional de Seguir/Dejar de seguir)
* **Qué prueba:** Es uno de los flujos más complejos del sistema. Prueba que la transacción actualiza simultáneamente los documentos de dos usuarios distintos (`currentUserRef` y `targetUserRef`), crea/elimina documentos en las subcolecciones `following`/`followers`, e incrementa/decrementa los contadores `followingCount` y `followersCount` de forma atómica.
