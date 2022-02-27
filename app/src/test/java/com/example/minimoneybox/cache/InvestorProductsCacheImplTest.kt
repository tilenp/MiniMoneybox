package com.example.minimoneybox.cache

import com.example.minimoneybox.cache.impl.InvestorProductsCacheImpl
import com.example.minimoneybox.model.local.*
import org.junit.Test

class InvestorProductsCacheImplTest {

    private val id1 = 1L
    private val id2 = 2L
    private val product1 = Product(
        id = id1,
        planValue = Amount(),
        moneybox = Amount(),
        data = ProductData()
    )
    private val product2 = Product(
        id = id2,
        planValue = Amount(),
        moneybox = Amount(),
        data = ProductData()
    )
    private val investorProducts = InvestorProducts(
        totalPlanValue = Amount(),
        products = listOf(product1, product2)
    )

    @Test
    fun investor_products_are_cached_correctly() {
        // arrange
        val investorProductsCache = InvestorProductsCacheImpl()

        // act
        investorProductsCache.writeInvestorProducts(investorProducts)
            .test()
            .dispose()

        // assert
        investorProductsCache.readInvestorProducts()
            .test()
            .assertValue(investorProducts)
            .dispose()
    }

    @Test
    fun selected_product_is_correct() {
        // arrange
        val investorProductsCache = InvestorProductsCacheImpl()

        // act
        investorProductsCache.writeInvestorProducts(investorProducts)
            .test()
            .dispose()
        investorProductsCache.selectProduct(id2)
            .test()
            .dispose()

        // assert
        investorProductsCache.getSelectedProduct()
            .test()
            .assertValue(product2)
            .dispose()
    }

    @Test
    fun moneybox_amount_is_added_correctly() {
        // arrange
        val newAmount = Amount(10.0, "£10.0")
        val moneybox = Moneybox(newAmount)
        val investorProductsCache = InvestorProductsCacheImpl()

        // act
        investorProductsCache.writeInvestorProducts(investorProducts)
            .test()
            .dispose()
        investorProductsCache.addAmount(moneybox, id1)
            .test()
            .dispose()

        // assert
        investorProductsCache.readInvestorProducts()
            .map { it.products.first { it.id == id1 } }
            .map { it.moneybox }
            .test()
            .assertValue(newAmount)
            .dispose()
    }
}