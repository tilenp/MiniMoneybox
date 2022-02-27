package com.example.minimoneybox.ui.account.individual_account

import androidx.annotation.StringRes

sealed class IndividualAccountUIState {
    object Loading : IndividualAccountUIState()
    object NotLoading : IndividualAccountUIState()
    data class Content(val content: IndividualAccountContent) : IndividualAccountUIState()
    data class ServerMessage(val message: String): IndividualAccountUIState()
    data class Message(@StringRes val messageId: Int): IndividualAccountUIState()
}