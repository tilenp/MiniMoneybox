package com.example.minimoneybox.mapper.local

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.ProductData
import com.example.minimoneybox.model.response.ProductResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataMapper @Inject constructor() : Mapper<ProductResponseData?, ProductData> {

    override fun map(objectToMap: ProductResponseData?): ProductData {
        return ProductData(objectToMap?.FriendlyName.orEmpty())
    }
}