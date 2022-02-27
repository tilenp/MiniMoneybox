package com.example.minimoneybox.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface UserRepository {

    fun setUserName(name: String): Completable

    fun getUserName(): Observable<String>
}