package com.example.minimoneybox.ui.authentication.login

import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.repository.UserRepository
import com.example.minimoneybox.ui.authentication.AuthenticationUIState
import com.example.minimoneybox.utils.TestSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class LoginViewModelTest {

    @Test
    fun authorization_data_is_sent_correctly() {
        // arrange
        val email = "email"
        val password = "password"
        val name = "name"
        val authorizationData = AuthorizationData(email, password, name)

        val loginRepository: LoginRepository = mockk()
        val userRepository: UserRepository = mockk()
        every { loginRepository.login(any()) } returns Single.just(Response.Success(Session()))
        every { userRepository.setUserName(any()) } returns Completable.complete()

        val viewModel = LoginViewModel(
            loginRepository = loginRepository,
            userRepository = userRepository,
            errorMessageHandler = mockk(),
            schedulerProvider = TestSchedulerProvider()
        )

        // act
        viewModel.login(email, password, name)

        // assert
        verify(exactly = 1) { loginRepository.login(authorizationData) }
    }

    @Test
    fun test_login_success() {
        // arrange
        val email = "email"
        val password = "password"
        val name = "name"

        val loginRepository: LoginRepository = mockk()
        val userRepository: UserRepository = mockk()
        every { loginRepository.login(any()) } returns Single.just(Response.Success(Session()))
        every { userRepository.setUserName(any()) } returns Completable.complete()

        val viewModel = LoginViewModel(
            loginRepository = loginRepository,
            userRepository = userRepository,
            errorMessageHandler = mockk(),
            schedulerProvider = TestSchedulerProvider()
        )

        val uiStateObserver = viewModel.observeUIState().test()

        // act
        viewModel.login(email, password, name)

        // assert
        uiStateObserver
            .assertValueAt(0, AuthenticationUIState.Loading)
            .assertValueAt(1, AuthenticationUIState.NotLoading)
            .dispose()
    }

    @Test
    fun user_name_is_cached_correctly() {
        // arrange
        val email = "email"
        val password = "password"
        val name = "name"

        val loginRepository: LoginRepository = mockk()
        val userRepository: UserRepository = mockk()
        every { loginRepository.login(any()) } returns Single.just(Response.Success(Session()))
        every { userRepository.setUserName(any()) } returns Completable.complete()

        val viewModel = LoginViewModel(
            loginRepository = loginRepository,
            userRepository = userRepository,
            errorMessageHandler = mockk(),
            schedulerProvider = TestSchedulerProvider()
        )

        // act
        viewModel.login(email, password, name)

        // assert
        verify(exactly = 1) { userRepository.setUserName(name) }
    }

    @Test
    fun test_login_server_error() {
        // arrange
        val email = "email"
        val password = "password"
        val name = "name"
        val errorBody = ErrorBody(
            Name = "Login failed",
            Message = "Incorrect email address or password. Please check and try again.",
            ValidationErrors = emptyList()
        )

        val loginRepository: LoginRepository = mockk()
        val userRepository: UserRepository = mockk()
        every { loginRepository.login(any()) } returns Single.just(Response.Error(errorBody))
        every { userRepository.setUserName(any()) } returns Completable.complete()

        val viewModel = LoginViewModel(
            loginRepository = loginRepository,
            userRepository = userRepository,
            errorMessageHandler = mockk(),
            schedulerProvider = TestSchedulerProvider()
        )

        val uiStateObserver = viewModel.observeUIState().test()

        // act
        viewModel.login(email, password, name)

        // assert
        uiStateObserver
            .assertValueAt(0, AuthenticationUIState.Loading)
            .assertValueAt(1, AuthenticationUIState.Error(errorBody))
            .assertValueAt(2, AuthenticationUIState.NotLoading)
            .dispose()
    }
}