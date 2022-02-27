package com.example.minimoneybox.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface AuthenticationRepository {

    fun authenticationCompleted(): Observable<Boolean>

    fun readBearerToken(): Observable<String>

    fun logOut(): Completable
}