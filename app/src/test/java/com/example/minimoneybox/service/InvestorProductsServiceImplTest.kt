package com.example.minimoneybox.service

import com.example.minimoneybox.FileReader
import com.example.minimoneybox.mapper.local.AmountMapper
import com.example.minimoneybox.mapper.local.InvestorProductsMapper
import com.example.minimoneybox.mapper.local.ProductDataMapper
import com.example.minimoneybox.mapper.local.ProductMapper
import com.example.minimoneybox.mapper.response.ErrorBodyMapper
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.local.ProductData
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.network.BearerTokenInterceptor
import com.example.minimoneybox.network.HeadersInterceptor
import com.example.minimoneybox.network.MoneyboxApi
import com.example.minimoneybox.network.MoneyboxAuthenticator
import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.service.impl.InvestorProductsServiceImpl
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InvestorProductsServiceImplTest {

    private val bearerToken = "BearerToken"
    private lateinit var webServer: MockWebServer
    private lateinit var investorProductsService: InvestorProductsService

    @Before
    fun setUp() {
        val authenticationRepository: AuthenticationRepository = mockk()
        every { authenticationRepository.readBearerToken() } returns Observable.just(bearerToken)
        every { authenticationRepository.logOut() } returns Completable.complete()

        webServer = MockWebServer()
        val serverURL = webServer.url("/").toString()

        val client = OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .addInterceptor(BearerTokenInterceptor(authenticationRepository))
            .authenticator(MoneyboxAuthenticator(authenticationRepository))
            .build()

        val gson = GsonBuilder()
            .create()

        val moneyboxApi = Retrofit.Builder()
            .baseUrl(serverURL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MoneyboxApi::class.java)

        val amountMapper = AmountMapper(mockk())
        val productDataMapper = ProductDataMapper()
        val productMapper = ProductMapper(amountMapper, productDataMapper)
        investorProductsService = InvestorProductsServiceImpl(
            moneyboxApi,
            InvestorProductsMapper(amountMapper, productMapper),
            ErrorBodyMapper(gson)
        )
    }

    @After
    fun cleanUp() {
        webServer.shutdown()
    }

    @Test
    fun add_amount_successful_response() {
        // arrange
        val json = FileReader.readFile("response_investor_products_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)
        val product1 = Product(
            id = 6137,
            planValue = Amount(32652.26, "£32652.26"),
            moneybox = Amount(560.00, "£560.0"),
            data = ProductData("Stocks & Shares ISA")
        )
        val product2 = Product(
            id = 6136,
            planValue = Amount(3514.49, "£3514.49"),
            moneybox = Amount(0.0, "£0.0"),
            data = ProductData("Lifetime ISA")
        )
        val investorProducts = InvestorProducts(
            totalPlanValue = Amount(63096.46, "£63096.46"),
            products = listOf(product1, product2)
        )

        // assert
        investorProductsService.getInvestorProduct()
            .test()
            .assertValue(Response.Success(investorProducts))
            .assertComplete()
            .dispose()
    }
}