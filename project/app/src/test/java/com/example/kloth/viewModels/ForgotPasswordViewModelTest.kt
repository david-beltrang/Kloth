package com.example.kloth.viewModels

import android.content.Context
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordStep
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var context: Context
    private lateinit var repository: AuthRepository
    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)
        every { context.getString(any()) } answers {
            when (firstArg<Int>()) {
                R.string.forgot_password_error_invalid_email -> "Correo inválido"
                R.string.forgot_password_error_invalid_code -> "Código inválido"
                R.string.auth_error_password_short -> "Contraseña corta"
                R.string.forgot_password_error_password_mismatch -> "No coinciden"
                R.string.forgot_password_request_success -> "Si está registrada, enviamos un código"
                R.string.forgot_password_reset_success -> "¡Contraseña restablecida con éxito!"
                R.string.auth_error_generic -> "Error genérico"
                else -> "Error"
            }
        }
        repository = mockk(relaxed = true)
        viewModel = ForgotPasswordViewModel(context, repository)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun requestCode_validEmail_movesToCodeStepAndStartsCooldown() = runTest(testDispatcher) {
        coEvery { repository.requestPasswordResetCode("ana@example.com") } returns Result.success(Unit)
        viewModel.onEmailChange("ana@example.com")

        viewModel.onSendClick()
        runCurrent()

        assertThat(viewModel.uiState.value.step).isEqualTo(ForgotPasswordStep.CodeAndPassword)
        assertThat(viewModel.uiState.value.resendSeconds).isEqualTo(60)
        assertThat(viewModel.uiState.value.showMessage).isTrue()
    }

    @Test
    fun requestCode_invalidEmail_showsValidationAndDoesNotCallRepository() {
        viewModel.onEmailChange("not-an-email")
        viewModel.onSendClick()

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Correo inválido")
        assertThat(viewModel.uiState.value.showMessage).isTrue()
    }

    @Test
    fun reset_invalidCode_doesNotCallRepository() {
        viewModel.onCodeChange("123")
        viewModel.onResetClick()

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Código inválido")
    }

    @Test
    fun reset_shortPassword_showsValidation() {
        viewModel.onCodeChange("123456")
        viewModel.onNewPasswordChange("123")
        viewModel.onConfirmPasswordChange("123")
        viewModel.onResetClick()

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Contraseña corta")
    }

    @Test
    fun reset_mismatchedPasswords_showsValidation() {
        viewModel.onCodeChange("123456")
        viewModel.onNewPasswordChange("abcdef")
        viewModel.onConfirmPasswordChange("ghijkl")
        viewModel.onResetClick()

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("No coinciden")
    }

    @Test
    fun reset_repositoryError_showsGenericMessage() = runTest(testDispatcher) {
        coEvery { repository.resetPasswordWithCode(any(), any(), any()) } returns
            Result.failure(IllegalStateException("Error genérico"))
        viewModel.onEmailChange("ana@example.com")
        viewModel.onCodeChange("123456")
        viewModel.onNewPasswordChange("abcdef")
        viewModel.onConfirmPasswordChange("abcdef")

        viewModel.onResetClick()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Error genérico")
        assertThat(viewModel.uiState.value.showMessage).isTrue()
        assertThat(viewModel.uiState.value.navigateBack).isFalse()
    }

    @Test
    fun reset_repositorySuccess_requestsNavigationBack() = runTest(testDispatcher) {
        coEvery { repository.resetPasswordWithCode(any(), any(), any()) } returns Result.success(Unit)
        viewModel.onEmailChange("ana@example.com")
        viewModel.onCodeChange("123456")
        viewModel.onNewPasswordChange("abcdef")
        viewModel.onConfirmPasswordChange("abcdef")

        viewModel.onResetClick()
        runCurrent()

        assertThat(viewModel.uiState.value.navigateBack).isFalse()
        assertThat(viewModel.uiState.value.showMessage).isTrue()
        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("¡Contraseña restablecida con éxito!")
        advanceTimeBy(2_000)
        runCurrent()
        assertThat(viewModel.uiState.value.navigateBack).isTrue()
        viewModel.onNavigationConsumed()
        assertThat(viewModel.uiState.value.navigateBack).isFalse()
    }

    @Test
    fun resend_waitsUntilCooldownEnds() = runTest(testDispatcher) {
        coEvery { repository.requestPasswordResetCode(any()) } returns Result.success(Unit)
        viewModel.onEmailChange("ana@example.com")
        viewModel.onSendClick()
        runCurrent()

        viewModel.onResendClick()
        runCurrent()
        assertThat(viewModel.uiState.value.resendSeconds).isEqualTo(60)

        advanceTimeBy(60_000)
        runCurrent()
        assertThat(viewModel.uiState.value.resendSeconds).isEqualTo(0)
        viewModel.onResendClick()
        runCurrent()
        assertThat(viewModel.uiState.value.resendSeconds).isEqualTo(60)
    }
}
