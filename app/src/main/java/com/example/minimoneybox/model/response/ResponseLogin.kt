package com.example.minimoneybox.model.response

data class ResponseLogin (
    val Session: ResponseSession?
)

data class ResponseSession (
    val BearerToken: String?
)