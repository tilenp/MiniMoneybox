package com.example.minimoneybox.model.response

data class ResponseInvestorsProduct (
    val TotalPlanValue: Double?,
    val ProductResponses: List<ProductResponse>?
)

data class ProductResponse (
    val Id: Long?,
    val PlanValue: Double?,
    val Moneybox: Double?,
    val Product: ProductResponseData?
)

data class ProductResponseData(
    val FriendlyName: String?
)