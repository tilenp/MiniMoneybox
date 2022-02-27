package com.example.minimoneybox.ui.account.user_accounts

import com.example.minimoneybox.model.local.Product

data class ProductsContent (
    val userName: String? = null,
    val totalPlanValue: String? = null,
    val products: List<Product> = emptyList()
)