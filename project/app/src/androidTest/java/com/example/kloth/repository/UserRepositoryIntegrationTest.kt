package com.example.kloth.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.dataSourceTest.grantLocalNetworkPermissionForFirebaseEmulator
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private lateinit var userRepository: UserRepository

    // Genera usuarios de prueba (equivalente al generateUser del video)
    private fun generateUser(i: Int): UserFirestoreDto = UserFirestoreDto(
        userId = "user_$i",
        username = "Name $i",
        email = "user$i@example.com",
        fullName = "Name $i",
        biography = "Bio $i",
        location = "City $i",
        website = "https://example.com/$i",
        followersCount = 0,
        followingCount = 0
    )

    @Before
    fun setUp() = runTest {
        grantLocalNetworkPermissionForFirebaseEmulator()
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
        }

        // Inyeccion manual (sin Hilt), igual que en clase
        userRepository = UserRepository(UserFirestoreDataSourceImpl(db))

        limpiarDatos()

        // replicables
        // autonomas
        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.userId), user)
        }
        batch.commit().await()
        Log.d("UserRepositoryIntegrationTest", "Despues del batch")
    }

    @After
    fun tearDown() = runTest {
        // Limpia users + subcolecciones followers/following
        limpiarDatos()
    }

    private suspend fun limpiarDatos() {
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (followingUser in following) {
                followingUser.reference.delete().await()
            }
            userDoc.reference.delete().await()
        }
    }

    @Test
    fun getUserById_validId_correctUser() = runTest {
        // arrange
        val id = "user_9"
        val expectedName = "Name 9"

        // act
        // dto -> mapper -> ui
        // Result
        // try catch
        val result = userRepository.getUserById(id)

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.username).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        // arrange
        val id = "user_999"

        // act
        val result = userRepository.getUserById(id)

        // assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("No se pudo obtener el usuario")
    }

    @Test
    fun updateProfile_validFields_profileUpdated() = runTest {
        // arrange
        val id = "user_1"

        // act
        val updateResult = userRepository.updateProfile(
            userId = id,
            username = "  username_actualizado  ",
            biography = "Nueva bio",
            location = "Bogotá",
            website = "https://kloth.test"
        )
        val profileResult = userRepository.getUserById(id)

        // assert
        Truth.assertThat(updateResult.isSuccess).isTrue()
        Truth.assertThat(profileResult.isSuccess).isTrue()
        Truth.assertThat(profileResult.getOrNull()?.username).isEqualTo("username_actualizado")
        Truth.assertThat(profileResult.getOrNull()?.bio).isEqualTo("Nueva bio")
        Truth.assertThat(profileResult.getOrNull()?.location).isEqualTo("Bogotá")
    }

    @Test
    fun followOrUnfollowUser_followUser_userFollowed() = runTest {
        // arrange
        val currentUserId = "user_0"
        val targetUserId = "user_1"

        // act
        val followResult = userRepository.followOrUnfollowUser(currentUserId, targetUserId)
        val isFollowingResult = userRepository.isFollowing(currentUserId, targetUserId)

        // assert
        Truth.assertThat(followResult.isSuccess).isTrue()
        Truth.assertThat(followResult.getOrNull()?.isFollowing).isTrue()
        Truth.assertThat(followResult.getOrNull()?.followersCount).isEqualTo(1)
        Truth.assertThat(isFollowingResult.getOrNull()).isTrue()
    }
}
