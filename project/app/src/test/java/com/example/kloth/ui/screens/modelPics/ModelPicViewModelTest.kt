package com.example.kloth.ui.screens.modelPics

import com.example.kloth.data.local.ModelPic
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ModelPicRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ModelPicViewModelTest {
    private val mainDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<ModelPicRepository>()
    private val authRepository = mockk<AuthRepository>()
    private val user = mockk<FirebaseUser>()
    private lateinit var viewModel: ModelPicViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        every { authRepository.currentUser } returns user
        every { user.uid } returns "user-1"
        viewModel = ModelPicViewModel(repository, authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadModelPics_loadsItemsAndFiltersBySelectedType() = runTest(mainDispatcher) {
        coEvery { repository.getModelPics("user-1") } returns Result.success(
            listOf(
                modelPic("base-1", ModelPicType.BASE),
                modelPic("generated-1", ModelPicType.GENERATED)
            )
        )

        viewModel.loadModelPics()

        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.modelPics).hasSize(2)
        assertThat(viewModel.uiState.value.visibleModelPics.map { it.id })
            .containsExactly("base-1")

        viewModel.selectFilter(ModelPicType.GENERATED)

        assertThat(viewModel.uiState.value.visibleModelPics.map { it.id })
            .containsExactly("generated-1")
    }

    @Test
    fun loadModelPics_withoutSignedInUserShowsError() = runTest(mainDispatcher) {
        every { authRepository.currentUser } returns null

        viewModel.loadModelPics()

        assertThat(viewModel.uiState.value.modelPics).isEmpty()
        assertThat(viewModel.uiState.value.errorMessage)
            .isEqualTo("Inicia sesión para ver tus model pics")
    }

    private fun modelPic(id: String, type: ModelPicType) = ModelPic(
        id = id,
        ownerId = "user-1",
        name = id,
        imageUrl = "https://storage.example/$id.jpg",
        storagePath = "modelPics/user-1/$id.jpg",
        type = type
    )
}
