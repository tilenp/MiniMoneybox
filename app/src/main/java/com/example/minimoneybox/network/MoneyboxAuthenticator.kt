package com.example.minimoneybox.network

import com.example.minimoneybox.repository.AuthenticationRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoneyboxAuthenticator @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        authenticationRepository.logOut().blockingAwait()
        return null
    }
}