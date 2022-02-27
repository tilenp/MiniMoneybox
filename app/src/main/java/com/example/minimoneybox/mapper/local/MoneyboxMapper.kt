package com.example.minimoneybox.mapper.local

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.response.ResponseOneOffPayments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoneyboxMapper @Inject constructor(
    private val amountMapper: AmountMapper
) : Mapper<ResponseOneOffPayments, Moneybox> {

    override fun map(objectToMap: ResponseOneOffPayments): Moneybox {
        return Moneybox(
            moneybox = amountMapper.map(objectToMap.Moneybox)
        )
    }
}