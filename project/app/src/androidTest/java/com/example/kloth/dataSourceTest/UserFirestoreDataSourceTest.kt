package com.example.kloth.dataSourceTest

import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserFirestoreDataSourceTest {

    private val testUserIds = listOf(
        "user_register_test",
        "user_profile_test",
        "user_follow_current",
        "user_follow_target"
    )
    private val db = Firebase.firestore
    private lateinit var datasource: UserFirestoreDataSourceImpl

    @Before
    fun setup() = runTest {
        grantLocalNetworkPermissionForFirebaseEmulator()
        datasource = UserFirestoreDataSourceImpl(db)
        limpiarDatos()
    }

    @After
    fun tearDown() = runTest {
        // Elimina los perfiles y documentos de subcoleccion creados por la prueba.
        limpiarDatos()
    }

    private suspend fun limpiarDatos() {
        for (userId in testUserIds) {
            val userRef = db.collection("users").document(userId)
            val followers = userRef.collection("followers").get().await()
            for (follower in followers) follower.reference.delete().await()
            val following = userRef.collection("following").get().await()
            for (followingUser in following) followingUser.reference.delete().await()
            userRef.delete().await()
        }
    }

    @Test
    fun testRegisterAndGetUserById() = runTest {
        // Arrange
        val userId = "user_register_test"
        val user = RegisterUserDto(
            email = "integration@example.com",
            password = "password",
            fullName = "Usuario Integración",
            username = "usuario_integration",
            biography = "Perfil de prueba",
            fcmToken = ""
        )

        // Act
        // Registra con el ID indicado y luego recupera el perfil usando el datasource.
        datasource.registerUser(user, userId)
        val result = datasource.getUserById(userId).toUserProfileInfo()

        // Assert
        // El perfil recuperado conserva el ID, nombre de usuario y correo registrados.
        Truth.assertThat(result.id).isEqualTo(userId)
        Truth.assertThat(result.username).isEqualTo(user.username)
        Truth.assertThat(result.email).isEqualTo(user.email)
        Truth.assertThat(db.collection("users").document(userId).get().await().exists()).isTrue()
    }

    @Test
    fun testUpdateProfileWithMerge() = runTest {
        // Arrange
        val userId = "user_profile_test"
        val userRef = db.collection("users").document(userId)
        userRef.set(
            UserFirestoreDto(
                userId = userId,
                username = "usuario_original",
                fullName = "Nombre conservado",
                email = "original@example.com",
                biography = "Biografía anterior",
                website = "https://old.example.com",
                location = "Colombia"
            )
        ).await()

        // Act
        // Actualiza los campos editables, elimina los opcionales vacios y conserva los demas.
        datasource.updateProfile(
            userId = userId,
            username = "  usuario_actualizado  ",
            biography = "   ",
            location = "  Bogotá  ",
            website = "",
            email = null
        )
        val result = userRef.get().await()

        // Assert
        // Comprueba los campos normalizados, los opcionales eliminados y los datos conservados.
        Truth.assertThat(result.getString("username")).isEqualTo("usuario_actualizado")
        Truth.assertThat(result.getString("location")).isEqualTo("Bogotá")
        Truth.assertThat(result.getString("biography")).isNull()
        Truth.assertThat(result.getString("website")).isNull()
        Truth.assertThat(result.getString("fullName")).isEqualTo("Nombre conservado")
        Truth.assertThat(result.getString("email")).isEqualTo("original@example.com")
    }

    @Test
    fun testFollowOrUnfollowUser() = runTest {
        // Arrange
        val currentUserId = "user_follow_current"
        val targetUserId = "user_follow_target"
        db.collection("users").document(currentUserId).set(
            UserFirestoreDto(userId = currentUserId, username = "current")
        ).await()
        db.collection("users").document(targetUserId).set(
            UserFirestoreDto(userId = targetUserId, username = "target")
        ).await()

        // Act
        // La primera llamada sigue al usuario y la segunda elimina esa relacion.
        val followResult = datasource.followOrUnfollowUser(currentUserId, targetUserId)
        val followingLink = db.collection("users").document(currentUserId)
            .collection("following").document(targetUserId).get().await()
        val followersLink = db.collection("users").document(targetUserId)
            .collection("followers").document(currentUserId).get().await()
        val followedTarget = db.collection("users").document(targetUserId).get().await()
        val unfollowResult = datasource.followOrUnfollowUser(currentUserId, targetUserId)
        val followingLinkAfterUnfollow = db.collection("users").document(currentUserId)
            .collection("following").document(targetUserId).get().await()
        val followersLinkAfterUnfollow = db.collection("users").document(targetUserId)
            .collection("followers").document(currentUserId).get().await()
        val currentUserAfterUnfollow = db.collection("users").document(currentUserId).get().await()
        val targetUserAfterUnfollow = db.collection("users").document(targetUserId).get().await()

        // Assert
        // Comprueba los enlaces en ambas subcolecciones y los contadores tras cada cambio.
        Truth.assertThat(followResult.isFollowing).isTrue()
        Truth.assertThat(followResult.followersCount).isEqualTo(1)
        Truth.assertThat(followingLink.exists()).isTrue()
        Truth.assertThat(followersLink.exists()).isTrue()
        Truth.assertThat(followedTarget.getLong("followersCount")).isEqualTo(1L)
        Truth.assertThat(unfollowResult.isFollowing).isFalse()
        Truth.assertThat(unfollowResult.followersCount).isEqualTo(0)
        Truth.assertThat(followingLinkAfterUnfollow.exists()).isFalse()
        Truth.assertThat(followersLinkAfterUnfollow.exists()).isFalse()
        Truth.assertThat(currentUserAfterUnfollow.getLong("followingCount")).isEqualTo(0L)
        Truth.assertThat(targetUserAfterUnfollow.getLong("followersCount")).isEqualTo(0L)
    }
}
