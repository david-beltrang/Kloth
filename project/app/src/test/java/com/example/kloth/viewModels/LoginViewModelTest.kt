package com.example.kloth.viewModels

import android.content.Context
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.screens.login.LoginViewModel
import com.google.common.truth.Truth.assertThat
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
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)
        every { context.getString(R.string.login_error_empty_fields) } returns
            "Por favor, completa todos los campos"
        every { context.getString(R.string.auth_error_password_short) } returns
            "La contraseña debe tener al menos 6 caracteres"
        every { context.getString(R.string.login_error_generic) } returns
            "Credenciales incorrectas o error de conexión"
        authRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        viewModel = LoginViewModel(context, authRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onEmailChange_updatesEmailInState() {
        // arrange / act
        viewModel.onEmailChange("juan@email.com")

        // assert
        assertThat(viewModel.uiState.value.email).isEqualTo("juan@email.com")
    }

    @Test
    fun togglePasswordVisibility_changesVisibilityFlag() {
        val inicial = viewModel.uiState.value.isPasswordVisible

        viewModel.togglePasswordVisibility()
        val cambiado = viewModel.uiState.value.isPasswordVisible

        assertNotEquals(inicial, cambiado)
    }

    @Test
    fun onLoginClick_emptyFields_showsRequiredError() {
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")

        viewModel.onLoginClick()

        val state = viewModel.uiState.value
        assertThat(state.showMessage).isTrue()
        assertThat(state.errorMessage).isEqualTo("Por favor, completa todos los campos")
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun onLoginClick_repoSuccess_setsNavigateTrue() = runTest {
        coEvery { authRepository.signIn("juan@email.com", "123456") } returns Result.success(Unit)
        every { authRepository.currentUser } returns null

        viewModel.onEmailChange("juan@email.com")
        viewModel.onPasswordChange("123456")
        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    @Test
    fun onLoginClick_repoFailure_setsErrorMessage() = runTest {
        coEvery { authRepository.signIn(any(), any()) } returns
            Result.failure(Exception("Credenciales inválidas"))

        viewModel.onEmailChange("test@email.com")
        viewModel.onPasswordChange("123456")
        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.errorMessage).isEqualTo("Credenciales inválidas")
        assertThat(state.showMessage).isTrue()
        assertThat(state.navigate).isFalse()
    }
}
