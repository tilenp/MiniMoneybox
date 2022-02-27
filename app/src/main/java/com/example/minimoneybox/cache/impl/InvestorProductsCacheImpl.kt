package com.example.minimoneybox.cache.impl

import com.example.minimoneybox.cache.InvestorProductsCache
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestorProductsCacheImpl @Inject constructor() : InvestorProductsCache {

    private val investorProductsSubject = BehaviorSubject.createDefault(INVESTOR_PRODUCTS)
    private val selectedProductSubject = BehaviorSubject.create<Long>()

    override fun readInvestorProducts(): Observable<InvestorProducts> {
        return investorProductsSubject.filter { it != INVESTOR_PRODUCTS }
    }

    override fun writeInvestorProducts(investorProducts: InvestorProducts): Completable {
        return Completable.fromCallable { investorProductsSubject.onNext(investorProducts) }
    }

    override fun selectProduct(productId: Long): Completable {
        return Completable.fromCallable { selectedProductSubject.onNext(productId) }
    }

    override fun getSelectedProduct(): Observable<Product> {
        return investorProductsSubject.withLatestFrom(selectedProductSubject) { investorProducts, selectedProductId ->
            investorProducts.products.first { it.id == selectedProductId }
        }
    }

    override fun addAmount(moneybox: Moneybox, productId: Long): Completable {
        return investorProductsSubject.firstOrError()
            .map { investorProducts ->
                val index = investorProducts.products.indexOfFirst { it.id == productId }
                investorProducts.products[index].moneybox = moneybox.moneybox
                investorProductsSubject.onNext(investorProducts)
            }.ignoreElement()
    }

    companion object {
        val INVESTOR_PRODUCTS = InvestorProducts()
    }
}