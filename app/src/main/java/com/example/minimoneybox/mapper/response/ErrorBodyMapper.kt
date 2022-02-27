package com.example.minimoneybox.mapper.response

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.response.ErrorBody
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorBodyMapper @Inject constructor(
    private val gson: Gson
) : Mapper<Throwable, ErrorBody> {

    override fun map(objectToMap: Throwable): ErrorBody {
        return when (objectToMap) {
            is HttpException -> deserializeErrorBody(objectToMap) ?: throw objectToMap
            else -> throw objectToMap
        }
    }

    private fun deserializeErrorBody(httpException: HttpException): ErrorBody? {
        return httpException.response()?.errorBody()?.string()?.let { json ->
            gson.fromJson(json, ErrorBody::class.java)
        }
    }
}