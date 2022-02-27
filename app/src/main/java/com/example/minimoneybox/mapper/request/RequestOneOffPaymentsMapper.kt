package com.example.minimoneybox.mapper.request

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.request.RequestOneOffPayments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestOneOffPaymentsMapper @Inject constructor() : Mapper<OneOffPayments, RequestOneOffPayments> {

    override fun map(objectToMap: OneOffPayments): RequestOneOffPayments {
        return RequestOneOffPayments(
            Amount = objectToMap.amount,
            InvestorProductId = objectToMap.investorProductId
        )
    }
}