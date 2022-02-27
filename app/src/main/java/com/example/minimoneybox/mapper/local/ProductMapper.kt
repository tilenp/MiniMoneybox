package com.example.minimoneybox.mapper.local

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.local.ProductData
import com.example.minimoneybox.model.response.ProductResponse
import com.example.minimoneybox.model.response.ProductResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductMapper @Inject constructor(
    private val amountMapper: Mapper<Double?, Amount>,
    private val productDataMapper: Mapper<ProductResponseData?, ProductData>
) : Mapper<ProductResponse, Product> {

    override fun map(objectToMap: ProductResponse): Product {
        return Product(
            id = objectToMap.Id ?: 0,
            planValue = amountMapper.map(objectToMap.PlanValue),
            moneybox = amountMapper.map(objectToMap.Moneybox),
            data = productDataMapper.map(objectToMap.Product)
        )
    }
}