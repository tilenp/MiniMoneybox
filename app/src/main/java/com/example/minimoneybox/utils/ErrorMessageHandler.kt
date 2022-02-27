package com.example.minimoneybox.utils

import com.example.minimoneybox.R
import io.reactivex.rxjava3.exceptions.CompositeException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMessageHandler @Inject constructor() {

    fun getExceptionMessage(throwable: Throwable?): Int {
        return  when (throwable) {
            is CompositeException -> getMessage(throwable.exceptions.lastOrNull())
            else -> getMessage(throwable)
        }
    }

    private fun getMessage(throwable: Throwable?): Int {
        return  when (throwable) {
            is IOException -> R.string.no_network_connection
            is HttpException -> getHttpExceptionMessage(throwable)
            else -> R.string.something_went_wrong
        }
    }

    private fun getHttpExceptionMessage(httpException: HttpException): Int {
        return when (httpException.code()) {
            401 -> R.string.session_expired
            in 500..599 -> R.string.Server_error
            else -> R.string.something_went_wrong
        }
    }
}