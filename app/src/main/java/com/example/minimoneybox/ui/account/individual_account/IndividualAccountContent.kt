package com.example.minimoneybox.ui.account.individual_account

import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.Product

data class IndividualAccountContent(
    var product: Product? = null,
    val amount: Amount = Amount(10.0, "Add £10")
)