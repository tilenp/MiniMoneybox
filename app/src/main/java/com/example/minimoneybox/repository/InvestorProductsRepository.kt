package com.example.minimoneybox.repository

import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface InvestorProductsRepository {

    fun loadInvestorProducts(): Single<Response<InvestorProducts, ErrorBody>>

    fun getInvestorProducts(): Observable<InvestorProducts>

    fun selectProduct(productId: Long): Completable

    fun getSelectedProduct(): Observable<Product>

    fun addAmount(amount: Double): Single<Response<Moneybox, ErrorBody>>
}