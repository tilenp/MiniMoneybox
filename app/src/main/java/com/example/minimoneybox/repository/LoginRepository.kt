package com.example.minimoneybox.repository

import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import io.reactivex.rxjava3.core.Single

interface LoginRepository {

    fun login(authorizationData: AuthorizationData): Single<Response<Session, ErrorBody>>
}