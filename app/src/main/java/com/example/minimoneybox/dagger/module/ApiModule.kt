package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.network.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Singleton
    @Provides
    fun providesRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory
            .create()
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory
            .create(gson)
    }

    @Singleton
    @Provides
    fun providesMoneyboxAuthenticationApi(
        logger: HttpLoggingInterceptor,
        headersInterceptor: HeadersInterceptor,
        callAdapterFactory: RxJava3CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): MoneyboxAuthenticationApi {
        return MoneyboxAuthenticationApi.create(
            logger,
            headersInterceptor,
            callAdapterFactory,
            converterFactory
        )
    }

    @Singleton
    @Provides
    fun providesMoneyboxApi(
        logger: HttpLoggingInterceptor,
        headersInterceptor: HeadersInterceptor,
        bearerTokenInterceptor: BearerTokenInterceptor,
        moneyboxAuthenticator: MoneyboxAuthenticator,
        callAdapterFactory: RxJava3CallAdapterFactory,
        converterFactory: GsonConverterFactory
    ): MoneyboxApi {
        return MoneyboxApi.create(
            logger,
            headersInterceptor,
            bearerTokenInterceptor,
            moneyboxAuthenticator,
            callAdapterFactory,
            converterFactory
        )
    }
}