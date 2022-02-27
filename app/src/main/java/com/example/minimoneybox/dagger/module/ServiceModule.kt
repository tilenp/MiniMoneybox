package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.service.InvestorProductsService
import com.example.minimoneybox.service.LoginService
import com.example.minimoneybox.service.OneOffPaymentsService
import com.example.minimoneybox.service.impl.InvestorProductsServiceImpl
import com.example.minimoneybox.service.impl.LoginServiceImpl
import com.example.minimoneybox.service.impl.OneOffPaymentsServiceImpl
import dagger.Binds
import dagger.Module

@Module
interface ServiceModule {

    @Binds
    fun bindsLoginService(loginServiceImpl: LoginServiceImpl): LoginService

    @Binds
    fun bindsInvestorProductsService(investorProductsServiceImpl: InvestorProductsServiceImpl): InvestorProductsService

    @Binds
    fun bindsOneOffPaymentsServiceImpl(oneOffPaymentsServiceImpl: OneOffPaymentsServiceImpl): OneOffPaymentsService
}