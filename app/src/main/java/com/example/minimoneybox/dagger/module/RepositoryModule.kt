package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.repository.UserRepository
import com.example.minimoneybox.repository.impl.AuthenticationRepositoryImpl
import com.example.minimoneybox.repository.impl.InvestorProductsRepositoryImpl
import com.example.minimoneybox.repository.impl.LoginRepositoryImpl
import com.example.minimoneybox.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindsReadAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindsInvestorProductsRepository(investorProductsRepositoryImpl: InvestorProductsRepositoryImpl): InvestorProductsRepository

    @Binds
    fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}