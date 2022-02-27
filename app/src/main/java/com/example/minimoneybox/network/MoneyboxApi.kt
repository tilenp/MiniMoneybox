package com.example.minimoneybox.network

import com.example.minimoneybox.model.request.RequestOneOffPayments
import com.example.minimoneybox.model.response.ResponseInvestorsProduct
import com.example.minimoneybox.model.response.ResponseOneOffPayments
import com.example.minimoneybox.utils.BASE_MONEYBOX_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MoneyboxApi {

    @GET("/investorproducts")
    fun getInvestorProducts(): Single<ResponseInvestorsProduct>

    @POST("/oneoffpayments")
    fun oneoffpayments(
        @Body requestOneOffPayments: RequestOneOffPayments
    ): Single<ResponseOneOffPayments>

    companion object {
        fun create(
            logger: HttpLoggingInterceptor,
            headersInterceptor: HeadersInterceptor,
            bearerTokenInterceptor: BearerTokenInterceptor,
            moneyboxAuthenticator: MoneyboxAuthenticator,
            callAdapterFactory: RxJava3CallAdapterFactory,
            converterFactory: GsonConverterFactory
        ): MoneyboxApi {
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(headersInterceptor)
                .addInterceptor(bearerTokenInterceptor)
                .authenticator(moneyboxAuthenticator)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_MONEYBOX_URL)
                .client(client)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(MoneyboxApi::class.java)
        }
    }
}