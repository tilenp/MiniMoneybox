package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.mapper.local.*
import com.example.minimoneybox.mapper.request.RequestAuthorizationDataMapper
import com.example.minimoneybox.mapper.request.RequestOneOffPaymentsMapper
import com.example.minimoneybox.mapper.response.ErrorBodyMapper
import com.example.minimoneybox.mapper.response.SessionMapper
import com.example.minimoneybox.model.local.*
import com.example.minimoneybox.model.request.RequestAuthorizationData
import com.example.minimoneybox.model.request.RequestOneOffPayments
import com.example.minimoneybox.model.response.*
import dagger.Binds
import dagger.Module

@Module
interface MapperModule {

    @Binds
    fun bindsRequestAuthorizationDataMapper(requestAuthorizationDataMapper: RequestAuthorizationDataMapper): Mapper<AuthorizationData, RequestAuthorizationData>

    @Binds
    fun bindsSessionMapper(sessionMapper: SessionMapper): Mapper<ResponseLogin, Session>

    @Binds
    fun bindsErrorBodyMapper(errorBodyMapper: ErrorBodyMapper): Mapper<Throwable, ErrorBody>

    @Binds
    fun bindsAmountMapper(amountMapper: AmountMapper): Mapper<Double?, Amount>

    @Binds
    fun bindsProductDataMapper(productDataMapper: ProductDataMapper): Mapper<ProductResponseData?, ProductData>

    @Binds
    fun bindsProductMapper(productMapper: ProductMapper): Mapper<ProductResponse, Product>

    @Binds
    fun bindsInvestorProductMapper(investorProductsMapper: InvestorProductsMapper): Mapper<ResponseInvestorsProduct, InvestorProducts>

    @Binds
    fun bindsRequestOneOffPaymentsMapper(requestOneOffPaymentsMapper: RequestOneOffPaymentsMapper): Mapper<OneOffPayments, RequestOneOffPayments>

    @Binds
    fun bindsMoneyboxMapper(moneyboxMapper: MoneyboxMapper): Mapper<ResponseOneOffPayments, Moneybox>
}