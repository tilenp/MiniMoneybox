package com.example.minimoneybox.network

import com.example.minimoneybox.model.request.RequestAuthorizationData
import com.example.minimoneybox.model.response.ResponseLogin
import com.example.minimoneybox.utils.BASE_MONEYBOX_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MoneyboxAuthenticationApi {

    @POST("/users/login")
    fun login(
        @Body requestAuthorizationData: RequestAuthorizationData
    ): Single<ResponseLogin>

    companion object {
        fun create(
            logger: HttpLoggingInterceptor,
            headersInterceptor: HeadersInterceptor,
            callAdapterFactory: RxJava3CallAdapterFactory,
            converterFactory: GsonConverterFactory
        ): MoneyboxAuthenticationApi {
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(headersInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_MONEYBOX_URL)
                .client(client)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(MoneyboxAuthenticationApi::class.java)
        }
    }
}