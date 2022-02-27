package com.example.minimoneybox.repository

import com.example.minimoneybox.cache.AuthenticationCache
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.impl.LoginRepositoryImpl
import com.example.minimoneybox.service.LoginService
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class LoginRepositoryImplTest {

    @Test
    fun successful_login_response_is_handled_correctly() {
        // arrange
        val loginService: LoginService = mockk()
        val authenticationCache: AuthenticationCache = mockk()

        val email = "email"
        val password = "password"
        val name = "name"
        val authorizationData = AuthorizationData(email, password, name)
        val session = Session()
        every { loginService.login(any()) } returns Single.just(Response.Success(session))
        every { authenticationCache.writeBearerToken(any()) } returns Completable.complete()
        val loginRepository = LoginRepositoryImpl(
            loginService  = loginService,
            authenticationCache = authenticationCache
        )

        // assert
        loginRepository.login(authorizationData)
            .test()
            .assertValue { it is Response.Success }
            .dispose()

        verify(exactly = 1) { loginService.login(authorizationData) }
        verify(exactly = 1) { authenticationCache.writeBearerToken(session) }
    }

    @Test
    fun error_login_response_is_handled_correctly() {
        // arrange
        val loginService: LoginService = mockk()
        val authenticationCache: AuthenticationCache = mockk()

        val email = "email"
        val password = "password"
        val name = "name"
        val authorizationData = AuthorizationData(email, password, name)
        val errorBody = ErrorBody("", "", emptyList())
        every { loginService.login(any()) } returns Single.just(Response.Error(errorBody))
        val loginRepository = LoginRepositoryImpl(
            loginService  = loginService,
            authenticationCache = authenticationCache
        )

        // assert
        loginRepository.login(authorizationData)
            .test()
            .assertValue { it is Response.Error }
            .dispose()

        verify(exactly = 1) { loginService.login(authorizationData) }
        verify { authenticationCache wasNot Called }
    }
}