package com.example.minimoneybox.service.impl

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.request.RequestAuthorizationData
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.model.response.ResponseLogin
import com.example.minimoneybox.network.MoneyboxAuthenticationApi
import com.example.minimoneybox.service.LoginService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginServiceImpl @Inject constructor(
    private val authorizationApi: MoneyboxAuthenticationApi,
    private val requestAuthorizationDataMapper: Mapper<AuthorizationData, RequestAuthorizationData>,
    private val sessionMapper: Mapper<ResponseLogin, Session>,
    private val errorBodyMapper: Mapper<Throwable, ErrorBody>
) : LoginService {

    override fun login(authorizationData: AuthorizationData): Single<Response<Session, ErrorBody>> {
        val requestAuthorizationData = requestAuthorizationDataMapper.map(authorizationData)
        return authorizationApi.login(requestAuthorizationData)
            .map { Response.Success(sessionMapper.map(it)) }
            .map { it as Response<Session, ErrorBody> }
            .onErrorReturn { Response.Error(errorBodyMapper.map(it)) }
    }
}