package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.cache.AuthenticationCache
import com.example.minimoneybox.cache.InvestorProductsCache
import com.example.minimoneybox.cache.UserCache
import com.example.minimoneybox.cache.impl.AuthenticationCacheImpl
import com.example.minimoneybox.cache.impl.InvestorProductsCacheImpl
import com.example.minimoneybox.cache.impl.UserCacheImpl
import dagger.Binds
import dagger.Module

@Module
interface CacheModule {

    @Binds
    fun bindsAuthenticationCache(authenticationCacheImpl: AuthenticationCacheImpl): AuthenticationCache

    @Binds
    fun bindsInvestorProductsCache(investorProductsCacheImpl: InvestorProductsCacheImpl): InvestorProductsCache

    @Binds
    fun bindsUserCache(userCacheImpl: UserCacheImpl): UserCache
}