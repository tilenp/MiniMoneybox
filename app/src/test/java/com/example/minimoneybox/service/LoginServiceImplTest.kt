package com.example.minimoneybox.service

import com.example.minimoneybox.FileReader
import com.example.minimoneybox.mapper.request.RequestAuthorizationDataMapper
import com.example.minimoneybox.mapper.response.ErrorBodyMapper
import com.example.minimoneybox.mapper.response.SessionMapper
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.model.response.ValidationError
import com.example.minimoneybox.network.HeadersInterceptor
import com.example.minimoneybox.network.MoneyboxAuthenticationApi
import com.example.minimoneybox.service.impl.LoginServiceImpl
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginServiceImplTest {

    private lateinit var webServer: MockWebServer
    private lateinit var loginService: LoginService

    @Before
    fun setUp() {
        webServer = MockWebServer()
        val serverURL = webServer.url("/").toString()

        val client = OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .build()

        val gson = GsonBuilder()
            .create()

        val moneyboxApi = Retrofit.Builder()
            .baseUrl(serverURL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MoneyboxAuthenticationApi::class.java)

        loginService = LoginServiceImpl(
            moneyboxApi,
            RequestAuthorizationDataMapper(),
            SessionMapper(),
            ErrorBodyMapper(gson)
        )
    }

    @After
    fun cleanUp() {
        webServer.shutdown()
    }

    @Test
    fun login_successful_response() {
        // arrange
        val json = FileReader.readFile("response_login_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)
        val session = Session(bearerToken = "QnvE8njqA5bsXpGisVTe0r/XAX92SNBI7tV91z770/k=")

        // assert
        loginService.login(AuthorizationData())
            .test()
            .assertValue(Response.Success(session))
            .assertComplete()
            .dispose()
    }

    @Test
    fun login_error_response() {
        // arrange
        val json = FileReader.readFile("response_login_400.json")
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(json)
        webServer.enqueue(mockResponse)
        val emailError = ValidationError(
            Name = "Email",
            Message = "The email address you entered is not valid.",
        )
        val passwordError = ValidationError(
            Name = "Password",
            Message = "Your password must be at least 10 characters"
        )
        val errorBody = ErrorBody(
            Name = "Validation failed",
            Message = "Please correct the following fields:",
            ValidationErrors = listOf(emailError, passwordError)
        )

        // assert
        loginService.login(AuthorizationData())
            .test()
            .assertValue(Response.Error(errorBody))
            .assertComplete()
            .dispose()
    }
}