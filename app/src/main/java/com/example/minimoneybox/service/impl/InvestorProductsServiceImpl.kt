package com.example.minimoneybox.service.impl

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.model.response.ResponseInvestorsProduct
import com.example.minimoneybox.network.MoneyboxApi
import com.example.minimoneybox.service.InvestorProductsService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestorProductsServiceImpl @Inject constructor(
    private val moneyboxApi: MoneyboxApi,
    private val investorProductsMapper: Mapper<ResponseInvestorsProduct, InvestorProducts>,
    private val errorBodyMapper: Mapper<Throwable, ErrorBody>
): InvestorProductsService {

    override fun getInvestorProduct(): Single<Response<InvestorProducts, ErrorBody>> {
        return moneyboxApi.getInvestorProducts()
            .map { Response.Success(investorProductsMapper.map(it)) }
            .map { it as Response<InvestorProducts, ErrorBody> }
            .onErrorReturn { Response.Error(errorBodyMapper.map(it)) }
    }
}