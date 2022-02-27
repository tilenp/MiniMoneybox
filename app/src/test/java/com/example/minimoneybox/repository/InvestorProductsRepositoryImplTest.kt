package com.example.minimoneybox.repository

import com.example.minimoneybox.cache.InvestorProductsCache
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.impl.InvestorProductsRepositoryImpl
import com.example.minimoneybox.service.InvestorProductsService
import com.example.minimoneybox.service.OneOffPaymentsService
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class InvestorProductsRepositoryImplTest {

    @Test
    fun investor_products_are_loaded_correctly() {
        // arrange
        val investorProductsService: InvestorProductsService = mockk()
        val investorProductsCache: InvestorProductsCache = mockk()

        val investorProducts = InvestorProducts()
        every { investorProductsService.getInvestorProduct() } returns Single.just(Response.Success(investorProducts))
        every { investorProductsCache.writeInvestorProducts(any()) } returns Completable.complete()
        val investorProductsRepository = InvestorProductsRepositoryImpl(
            investorProductsService = investorProductsService,
            oneOffPaymentsService = mockk(),
            investorProductsCache = investorProductsCache
        )

        // assert
        investorProductsRepository.loadInvestorProducts()
            .test()
            .assertValue { it is Response.Success }
            .dispose()

        verify(exactly = 1) { investorProductsCache.writeInvestorProducts(investorProducts) }
    }

    @Test
    fun investor_products_error_response_is_handled_correctly() {
        // arrange
        val investorProductsService: InvestorProductsService = mockk()
        val investorProductsCache: InvestorProductsCache = mockk()

        val errorBody = ErrorBody("", "", emptyList())
        every { investorProductsService.getInvestorProduct() } returns Single.just(Response.Error(errorBody))
        val investorProductsRepository = InvestorProductsRepositoryImpl(
            investorProductsService = investorProductsService,
            oneOffPaymentsService = mockk(),
            investorProductsCache = investorProductsCache
        )

        // assert
        investorProductsRepository.loadInvestorProducts()
            .test()
            .assertValue { it is Response.Error }
            .dispose()

        verify { investorProductsCache wasNot Called }
    }

    @Test
    fun one_off_payment_is_added_correctly() {
        // arrange
        val oneOffPaymentsService: OneOffPaymentsService = mockk()
        val investorProductsCache: InvestorProductsCache = mockk()

        val id = 1L
        val amount = 10.0
        val selectedProduct = Product(id = id)
        val moneybox = Moneybox()
        every { investorProductsCache.getSelectedProduct() } returns Observable.just(selectedProduct)
        every { oneOffPaymentsService.addAmount(any()) } returns Single.just(Response.Success(moneybox))
        every { investorProductsCache.addAmount(any(), any()) } returns Completable.complete()

        val investorProductsRepository = InvestorProductsRepositoryImpl(
            investorProductsService = mockk(),
            oneOffPaymentsService = oneOffPaymentsService,
            investorProductsCache = investorProductsCache
        )

        // assert
        investorProductsRepository.addAmount(amount)
            .test()
            .assertValue { it is Response.Success }
            .dispose()

        verify(exactly = 1) { oneOffPaymentsService.addAmount(OneOffPayments(amount, id)) }
        verify(exactly = 1) { investorProductsCache.addAmount(moneybox, id) }
    }

    @Test
    fun one_off_payment_error_response_is_handled_correctly() {
        // arrange
        val oneOffPaymentsService: OneOffPaymentsService = mockk()
        val investorProductsCache: InvestorProductsCache = mockk()

        val id = 1L
        val amount = 10.0
        val selectedProduct = Product(id = id)
        val errorBody = ErrorBody("", "", emptyList())
        every { investorProductsCache.getSelectedProduct() } returns Observable.just(selectedProduct)
        every { oneOffPaymentsService.addAmount(any()) } returns Single.just(Response.Error(errorBody))

        val investorProductsRepository = InvestorProductsRepositoryImpl(
            investorProductsService = mockk(),
            oneOffPaymentsService = oneOffPaymentsService,
            investorProductsCache = investorProductsCache
        )

        // assert
        investorProductsRepository.addAmount(amount)
            .test()
            .assertValue { it is Response.Error }
            .dispose()

        verify(exactly = 1) { investorProductsCache.getSelectedProduct() }
        verify(exactly = 1) { oneOffPaymentsService.addAmount(OneOffPayments(amount, id)) }
        verify { investorProductsCache.addAmount(any(), any()) wasNot Called }
    }
}