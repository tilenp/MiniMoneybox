package com.example.minimoneybox.service

import com.example.minimoneybox.FileReader
import com.example.minimoneybox.mapper.local.AmountMapper
import com.example.minimoneybox.mapper.local.MoneyboxMapper
import com.example.minimoneybox.mapper.request.RequestOneOffPaymentsMapper
import com.example.minimoneybox.mapper.response.ErrorBodyMapper
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.OneOffPayments
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.network.BearerTokenInterceptor
import com.example.minimoneybox.network.HeadersInterceptor
import com.example.minimoneybox.network.MoneyboxApi
import com.example.minimoneybox.network.MoneyboxAuthenticator
import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.service.impl.OneOffPaymentsServiceImpl
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

class OneOffPaymentsServiceImplTest {

    private val bearerToken = "BearerToken"
    private lateinit var webServer: MockWebServer
    private lateinit var oneOffPaymentsService: OneOffPaymentsService

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

        oneOffPaymentsService = OneOffPaymentsServiceImpl(
            moneyboxApi,
            RequestOneOffPaymentsMapper(),
            MoneyboxMapper(AmountMapper(mockk())),
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
        val json = FileReader.readFile("response_add_amount_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)
        val amount = Amount(30.00, "£30.0")
        val moneybox = Moneybox(amount)

        // assert
        oneOffPaymentsService.addAmount(OneOffPayments())
            .test()
            .assertValue(Response.Success(moneybox))
            .assertComplete()
            .dispose()
    }

    @Test
    fun add_amount_error_response() {
        // arrange
        val json = FileReader.readFile("response_add_amount_400.json")
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(json)
        webServer.enqueue(mockResponse)
        val errorBody = ErrorBody(
            Name = "Annual limit exceeded",
            Message = "Your annual Lifetime ISA limit is £4,000.00. You may add up to £0.00.",
            ValidationErrors = emptyList()
        )

        // assert
        oneOffPaymentsService.addAmount(OneOffPayments())
            .test()
            .assertValue(Response.Error(errorBody))
            .assertComplete()
            .dispose()
    }
}