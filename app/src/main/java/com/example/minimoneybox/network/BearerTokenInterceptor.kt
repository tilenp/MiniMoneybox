package com.example.minimoneybox.network

import com.example.minimoneybox.repository.AuthenticationRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BearerTokenInterceptor @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {
        val bearerToken = authenticationRepository.readBearerToken().blockingFirst()
        return newBuilder()
            .header("Authorization", "Bearer $bearerToken")
            .build()
    }
}