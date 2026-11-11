package com.example.kloth.viewModels

import android.content.Context
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.screens.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelUnitTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)
        every { context.getString(R.string.register_error_empty_fields) } returns
            "Todos los campos son obligatorios"
        every { context.getString(R.string.auth_error_password_short) } returns
            "La contraseña debe tener al menos 6 caracteres"
        every { context.getString(R.string.register_error_user_id) } returns
            "No se pudo obtener el ID del usuario"
        every { context.getString(R.string.register_error_save_user) } returns
            "No se pudo guardar el usuario"
        every { context.getString(R.string.register_error_generic) } returns
            "Error al registrar"
        authRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        viewModel = RegisterViewModel(context, authRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun registerButtonPressed_emptyFields_showsRequiredError() {
        viewModel.onFullNameChange("")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.onConfirmPasswordChange("")

        viewModel.registerButtonPressed()

        val state = viewModel.uiState.value
        assertThat(state.showMessage).isTrue()
        assertThat(state.errorMessage).isEqualTo("Todos los campos son obligatorios")
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun registerButtonPressed_shortPassword_showsPasswordError() {
        viewModel.onFullNameChange("Juan Perez")
        viewModel.onEmailChange("juan@test.com")
        viewModel.onPasswordChange("123")
        viewModel.onConfirmPasswordChange("123")

        viewModel.registerButtonPressed()

        val state = viewModel.uiState.value
        assertThat(state.showMessage).isTrue()
        assertThat(state.errorMessage).isEqualTo("La contraseña debe tener al menos 6 caracteres")
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun registerButtonPressed_repoSuccess_setsNavigateTrue() = runTest {
        val fakeUser = mockk<FirebaseUser>()
        every { fakeUser.uid } returns "fakeUserId123"
        every { authRepository.currentUser } returns fakeUser
        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)
        coEvery {
            userRepository.registerUser(any(), any(), any(), any())
        } returns Result.success(Unit)

        viewModel.onFullNameChange("Juan Perez")
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmPasswordChange("123456")

        viewModel.registerButtonPressed()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }
}
