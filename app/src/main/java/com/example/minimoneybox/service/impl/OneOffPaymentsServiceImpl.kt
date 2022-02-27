package com.example.minimoneybox.service.impl

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.request.RequestOneOffPayments
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.model.response.ResponseOneOffPayments
import com.example.minimoneybox.network.MoneyboxApi
import com.example.minimoneybox.service.OneOffPaymentsService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OneOffPaymentsServiceImpl @Inject constructor(
    private val moneyboxApi: MoneyboxApi,
    private val requestOneOffPaymentsMapper: Mapper<OneOffPayments, RequestOneOffPayments>,
    private val moneyboxMapper: Mapper<ResponseOneOffPayments, Moneybox>,
    private val errorBodyMapper: Mapper<Throwable, ErrorBody>
): OneOffPaymentsService {

    override fun addAmount(oneOffPayments: OneOffPayments): Single<Response<Moneybox, ErrorBody>> {
        val requestOneOffPayments = requestOneOffPaymentsMapper.map(oneOffPayments)
        return moneyboxApi.oneoffpayments(requestOneOffPayments)
            .map { Response.Success(moneyboxMapper.map(it)) }
            .map { it as Response<Moneybox, ErrorBody> }
            .onErrorReturn { Response.Error(errorBodyMapper.map(it)) }
    }
}