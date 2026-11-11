package com.example.kloth.ui.screens.tryOn

import com.example.kloth.data.local.ModelPic
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.local.TryOnResult
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ModelPicRepository
import com.example.kloth.data.repository.TryOnRepository
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
class TryOnViewModelTest {
    private val mainDispatcher = UnconfinedTestDispatcher()
    private val modelPicRepository = mockk<ModelPicRepository>()
    private val tryOnRepository = mockk<TryOnRepository>()
    private val authRepository = mockk<AuthRepository>()
    private val user = mockk<FirebaseUser>()
    private lateinit var viewModel: TryOnViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        every { authRepository.currentUser } returns user
        every { user.uid } returns "user-1"
        viewModel = TryOnViewModel(modelPicRepository, tryOnRepository, authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun openDialogLoadsOnlyBaseModelPicsAndAllowsSingleSelection() = runTest(mainDispatcher) {
        coEvery { modelPicRepository.getModelPics("user-1") } returns Result.success(
            listOf(
                modelPic("base-1", ModelPicType.BASE),
                modelPic("generated-1", ModelPicType.GENERATED)
            )
        )

        viewModel.openDialog("article-1", "https://example.com/garment.jpg")

        assertThat(viewModel.uiState.value.isDialogVisible).isTrue()
        assertThat(viewModel.uiState.value.publicationId).isEqualTo("article-1")
        assertThat(viewModel.uiState.value.baseModelPics.map { it.id })
            .containsExactly("base-1")

        viewModel.selectBaseModelPic("base-1")
        assertThat(viewModel.uiState.value.selectedBaseModelPicId).isEqualTo("base-1")
    }

    @Test
    fun requestGenerationShowsTheCallableResult() = runTest(mainDispatcher) {
        coEvery { modelPicRepository.getModelPics("user-1") } returns Result.success(
            listOf(modelPic("base-1", ModelPicType.BASE))
        )
        coEvery { tryOnRepository.generateTryOn("article-1", "base-1") } returns
            Result.success(
                TryOnResult(
                    modelPicId = "generated-1",
                    imageUrl = "https://storage.example/generated-1.jpg"
                )
            )
        viewModel.openDialog("article-1", "https://example.com/garment.jpg")
        viewModel.selectBaseModelPic("base-1")

        viewModel.requestGeneration()

        assertThat(viewModel.uiState.value.phase).isEqualTo(TryOnPhase.RESULT)
        assertThat(viewModel.uiState.value.resultModelPicId).isEqualTo("generated-1")
        assertThat(viewModel.uiState.value.resultImageUrl)
            .isEqualTo("https://storage.example/generated-1.jpg")
    }

    @Test
    fun requestGenerationShowsRepositoryFailure() = runTest(mainDispatcher) {
        coEvery { modelPicRepository.getModelPics("user-1") } returns
            Result.success(listOf(modelPic("base-1", ModelPicType.BASE)))
        coEvery { tryOnRepository.generateTryOn("article-1", "base-1") } returns
            Result.failure(IllegalStateException("FASHN no pudo generar la imagen"))

        viewModel.openDialog("article-1", "https://example.com/garment.jpg")
        viewModel.selectBaseModelPic("base-1")
        viewModel.requestGeneration()

        assertThat(viewModel.uiState.value.phase).isEqualTo(TryOnPhase.ERROR)
        assertThat(viewModel.uiState.value.errorMessage)
            .isEqualTo("FASHN no pudo generar la imagen")
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
