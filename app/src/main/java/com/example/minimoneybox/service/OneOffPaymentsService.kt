package com.example.minimoneybox.service

import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import io.reactivex.rxjava3.core.Single

interface OneOffPaymentsService {

    fun addAmount(oneOffPayments: OneOffPayments): Single<Response<Moneybox, ErrorBody>>
}