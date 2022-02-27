package com.example.minimoneybox.service

import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import io.reactivex.rxjava3.core.Single

interface InvestorProductsService {

    fun getInvestorProduct(): Single<Response<InvestorProducts, ErrorBody>>
}