package com.example.minimoneybox.cache

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface UserCache {

    fun setUserName(name: String): Completable

    fun getUserName(): Observable<String>
}