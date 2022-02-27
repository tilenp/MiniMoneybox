package com.example.minimoneybox.repository.impl

import com.example.minimoneybox.cache.AuthenticationCache
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.repository.AuthenticationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationCache: AuthenticationCache,
) : AuthenticationRepository {

    override fun authenticationCompleted(): Observable<Boolean> {
        return authenticationCache.readBearerToken().map { it.isNotBlank() }
    }

    override fun readBearerToken(): Observable<String> {
        return authenticationCache.readBearerToken()
    }

    override fun logOut(): Completable {
        return authenticationCache.writeBearerToken(Session())
    }
}