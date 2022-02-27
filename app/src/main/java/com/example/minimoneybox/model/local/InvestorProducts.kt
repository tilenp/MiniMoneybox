package com.example.minimoneybox.model.local

data class InvestorProducts (
    val totalPlanValue: Amount = Amount(),
    val products: List<Product> = emptyList()
)