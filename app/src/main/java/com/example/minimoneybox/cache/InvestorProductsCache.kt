package com.example.minimoneybox.cache

import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface InvestorProductsCache {

    fun readInvestorProducts(): Observable<InvestorProducts>

    fun writeInvestorProducts(investorProducts: InvestorProducts): Completable

    fun selectProduct(productId: Long): Completable

    fun getSelectedProduct(): Observable<Product>

    fun addAmount(moneybox: Moneybox, productId: Long): Completable
}