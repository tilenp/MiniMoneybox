package com.example.minimoneybox.repository.impl

import com.example.minimoneybox.cache.InvestorProductsCache
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.service.InvestorProductsService
import com.example.minimoneybox.service.OneOffPaymentsService
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestorProductsRepositoryImpl @Inject constructor(
    private val investorProductsService: InvestorProductsService,
    private val oneOffPaymentsService: OneOffPaymentsService,
    private val investorProductsCache: InvestorProductsCache
) : InvestorProductsRepository {

    override fun loadInvestorProducts(): Single<Response<InvestorProducts, ErrorBody>> {
        return investorProductsService.getInvestorProduct()
            .flatMap { handleInvestorProductsResponse(it) }
    }

    private fun handleInvestorProductsResponse(response: Response<InvestorProducts, ErrorBody>): Single<Response<InvestorProducts, ErrorBody>> {
        return when (response) {
            is Response.Success -> investorProductsCache.writeInvestorProducts(response.data).andThen(Single.just(response))
            is Response.Error -> Single.just(response)
        }
    }

    override fun getInvestorProducts(): Observable<InvestorProducts> {
        return investorProductsCache.readInvestorProducts()
    }

    override fun selectProduct(productId: Long): Completable {
        return investorProductsCache.selectProduct(productId)
    }

    override fun getSelectedProduct(): Observable<Product> {
        return investorProductsCache.getSelectedProduct()
    }

    override fun addAmount(amount: Double): Single<Response<Moneybox, ErrorBody>> {
        return investorProductsCache.getSelectedProduct()
            .firstOrError()
            .flatMap { product ->
                oneOffPaymentsService.addAmount(OneOffPayments(amount, product.id))
                    .flatMap { handleOneOffPaymentsResponse(it, product.id) }
            }
    }

    private fun handleOneOffPaymentsResponse(response: Response<Moneybox, ErrorBody>, productId: Long): Single<Response<Moneybox, ErrorBody>> {
        return when (response) {
            is Response.Success -> investorProductsCache.addAmount(response.data, productId).andThen(Single.just(response))
            is Response.Error -> Single.just(response)
        }
    }
}