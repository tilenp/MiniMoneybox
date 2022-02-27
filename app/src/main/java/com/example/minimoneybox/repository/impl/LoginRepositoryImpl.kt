package com.example.minimoneybox.repository.impl

import com.example.minimoneybox.cache.AuthenticationCache
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.service.LoginService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val authenticationCache: AuthenticationCache,
) : LoginRepository {

    override fun login(authorizationData: AuthorizationData): Single<Response<Session, ErrorBody>> {
        return loginService.login(authorizationData)
            .flatMap { handleSignUpResponse(it) }
    }

    private fun handleSignUpResponse(response: Response<Session, ErrorBody>): Single<Response<Session, ErrorBody>> {
        return when (response) {
            is Response.Success -> authenticationCache.writeBearerToken(response.data).andThen(Single.just(response))
            is Response.Error -> Single.just(response)
        }
    }
}