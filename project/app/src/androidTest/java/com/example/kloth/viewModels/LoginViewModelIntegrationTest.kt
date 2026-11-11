package com.example.kloth.viewModels

import androidx.test.core.app.ApplicationProvider
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.datasource.services.AuthRemoteDataSource
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.dataSourceTest.grantLocalNetworkPermissionForFirebaseEmulator
import com.example.kloth.ui.screens.login.LoginViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelIntegrationTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        grantLocalNetworkPermissionForFirebaseEmulator()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) {
        }

        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        userRepository = UserRepository(UserFirestoreDataSourceImpl(Firebase.firestore))
        authRepository = AuthRepository(authRemoteDataSource, context)
        viewModel = LoginViewModel(context, authRepository, userRepository)
    }

    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        runCatching { user?.delete()?.await() }
        Firebase.auth.signOut()
    }

    @Test
    fun login_success_navigates() = runTest {
        // arrange
        val email = "login_ok_${System.currentTimeMillis()}@test.com"
        val password = "123456"
        val signUp = authRepository.signUp(email, password)
        assertThat(signUp.isSuccess).isTrue()

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // act
        viewModel.onLoginClick()

        // Suspende en tiempo real hasta que navigate sea true
        val navigate = viewModel.uiState.map { it.navigate }.first { it }

        // assert
        assertThat(navigate).isTrue()
        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    @Test
    fun login_wrongPassword_showsError() = runTest {
        // arrange
        val email = "login_bad_${System.currentTimeMillis()}@test.com"
        val password = "123456"
        val signUp = authRepository.signUp(email, password)
        assertThat(signUp.isSuccess).isTrue()
        Firebase.auth.signOut()

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("wrong_password")

        // act
        viewModel.onLoginClick()

        val showMessage = viewModel.uiState.map { it.showMessage }.first { it }

        // assert
        assertThat(showMessage).isTrue()
        val state = viewModel.uiState.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
    }
}
