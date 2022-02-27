package com.example.minimoneybox.cache

import com.example.minimoneybox.model.local.Session
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface AuthenticationCache {

    fun readBearerToken(): Observable<String>

    fun writeBearerToken(session: Session): Completable
}