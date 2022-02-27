package com.example.minimoneybox.model.local

data class Product (
    val id: Long = 0,
    val planValue: Amount = Amount(),
    var moneybox: Amount = Amount(),
    val data: ProductData = ProductData()
)