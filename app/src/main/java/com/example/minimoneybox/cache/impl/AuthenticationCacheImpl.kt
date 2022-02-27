package com.example.minimoneybox.cache.impl

import com.example.minimoneybox.cache.AuthenticationCache
import com.example.minimoneybox.model.local.Session
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationCacheImpl @Inject constructor() : AuthenticationCache {

    private val sessionSubject = BehaviorSubject.createDefault(Session())

    override fun readBearerToken(): Observable<String> {
        return sessionSubject.map { it.bearerToken }
    }

    override fun writeBearerToken(session: Session): Completable {
        return Completable.fromCallable { sessionSubject.onNext(session) }
    }
}