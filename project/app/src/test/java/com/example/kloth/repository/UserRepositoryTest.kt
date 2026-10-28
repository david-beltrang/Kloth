package com.example.kloth.repository

import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.local.UserFollowState
import com.example.kloth.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

// Pruebas unitarias del repositorio (carpeta test): usan MockK, sin emulador.
class UserRepositoryTest {

    private val mockDataSource = mockk<UserFirestoreDataSourceImpl>()
    private val repository = UserRepository(mockDataSource)

    @Test
    fun getUserById_validId_correctUser() = runTest {
        // arrange
        val dto = UserFirestoreDto(
            userId = "user_1",
            username = "ana_modas",
            email = "ana@example.com",
            fullName = "Ana Modas",
            biography = "Bio de prueba",
            followersCount = 14614124
        )
        coEvery { mockDataSource.getUserById("user_1") } returns dto

        // act
        // El repositorio mapea DTO -> UserProfileInfo (UI)
        val result = repository.getUserById("user_1")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("user_1")
        Truth.assertThat(result.getOrNull()?.username).isEqualTo("ana_modas")
        Truth.assertThat(result.getOrNull()?.email).isEqualTo("ana@example.com")
        Truth.assertThat(result.getOrNull()?.bio).isEqualTo("Bio de prueba")
        Truth.assertThat(result.getOrNull()?.followersCount).isEqualTo(14614124)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        // arrange
        coEvery { mockDataSource.getUserById("user_999") } throws
            Exception("No se pudo obtener el usuario")

        // act
        val result = repository.getUserById("user_999")

        // assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("No se pudo obtener el usuario")
    }

    @Test
    fun followOrUnfollowUser_followUser_returnsFollowState() = runTest {
        // arrange
        coEvery {
            mockDataSource.followOrUnfollowUser("user_0", "user_1")
        } returns UserFollowState(isFollowing = true, followersCount = 1)

        // act
        val result = repository.followOrUnfollowUser("user_0", "user_1")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.isFollowing).isTrue()
        Truth.assertThat(result.getOrNull()?.followersCount).isEqualTo(1)
    }
}
