package com.example.minimoneybox.model.request

data class RequestAuthorizationData(
    val Email: String,
    val Password: String,
    val Idfa: String = "ANYTHING"
)