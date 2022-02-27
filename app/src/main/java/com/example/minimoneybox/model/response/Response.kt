package com.example.minimoneybox.model.response

sealed class Response<out Success, out Error> {
    data class Success<out Success>(val data: Success) : Response<Success, Nothing>()
    data class Error<out Error>(val error: Error) : Response<Nothing, Error>()
}

data class ErrorBody(
    val Name: String,
    val Message: String,
    val ValidationErrors: List<ValidationError>
)

data class ValidationError(
    val Name: String,
    val Message: String
)