package com.example.minimoneybox.network

import com.example.minimoneybox.API_VERSION
import com.example.minimoneybox.APP_ID
import com.example.minimoneybox.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadersInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {
        return newBuilder()
            .header("AppId", APP_ID)
            .header("Content-Type", "application/json")
            .header("appVersion", BuildConfig.VERSION_NAME)
            .header("apiVersion", API_VERSION)
            .build()
    }
}