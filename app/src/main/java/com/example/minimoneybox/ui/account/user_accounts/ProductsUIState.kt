package com.example.minimoneybox.ui.account.user_accounts

import androidx.annotation.StringRes

sealed class ProductsUIState {
    object Loading : ProductsUIState()
    data class Content(val content: ProductsContent) : ProductsUIState()
    data class Navigate(val actionId: Int): ProductsUIState()
    data class Message(@StringRes val messageId: Int): ProductsUIState()
    data class Retry(@StringRes val messageId: Int): ProductsUIState()
}