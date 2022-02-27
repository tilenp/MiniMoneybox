package com.example.minimoneybox.repository.impl

import com.example.minimoneybox.cache.UserCache
import com.example.minimoneybox.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userCache: UserCache,
) : UserRepository {

    override fun setUserName(name: String): Completable {
        return userCache.setUserName(name)
    }

    override fun getUserName(): Observable<String> {
        return userCache.getUserName()
    }
}