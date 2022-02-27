package com.example.minimoneybox.mapper.local

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ProductResponse
import com.example.minimoneybox.model.response.ResponseInvestorsProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestorProductsMapper @Inject constructor(
    private val amountMapper: Mapper<Double?, Amount>,
    private val productMapper: Mapper<ProductResponse, Product>
) : Mapper<ResponseInvestorsProduct, InvestorProducts> {

    override fun map(objectToMap: ResponseInvestorsProduct): InvestorProducts {
        return InvestorProducts(
            totalPlanValue = amountMapper.map(objectToMap.TotalPlanValue),
            products = objectToMap.ProductResponses?.map { productMapper.map(it) } ?: emptyList()
        )
    }
}