package com.example.minimoneybox.cache.impl

import com.example.minimoneybox.cache.UserCache
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCacheImpl @Inject constructor() : UserCache {

    private val userSubject = BehaviorSubject.create<String>()

    override fun setUserName(name: String): Completable {
        return Completable.fromCallable { userSubject.onNext(name) }
    }

    override fun getUserName(): Observable<String> {
        return userSubject
    }
}