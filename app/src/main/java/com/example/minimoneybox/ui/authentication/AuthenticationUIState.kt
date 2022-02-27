package com.example.minimoneybox.ui.authentication

import androidx.annotation.StringRes
import com.example.minimoneybox.model.response.ErrorBody

sealed class AuthenticationUIState {
    object Loading : AuthenticationUIState()
    object NotLoading : AuthenticationUIState()
    data class Error(val error: ErrorBody) : AuthenticationUIState()
    data class Message(@StringRes val messageId: Int): AuthenticationUIState()
}