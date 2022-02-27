package com.example.minimoneybox.ui.account.user_accounts

import com.example.minimoneybox.R
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.UserRepository
import com.example.minimoneybox.utils.TestSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test

class UserAccountsViewModelTest {

    @Test
    fun content_is_shown_correctly() {
        // arrange
        val userName = "userName"
        val totalPlanValue = Amount()
        val product = Product(1)
        val products = listOf(product)
        val investorProducts = InvestorProducts(
            totalPlanValue = totalPlanValue,
            products = products
        )
        val productsContent = ProductsContent(
            userName = userName,
            totalPlanValue = totalPlanValue.formattedAmount,
            products = products
        )
        val investorProductsRepository: InvestorProductsRepository = mockk()
        val userRepository: UserRepository = mockk()
        val authenticationRepository: AuthenticationRepository = mockk()

        every { investorProductsRepository.getInvestorProducts() } returns Observable.just(investorProducts)
        every { userRepository.getUserName() } returns Observable.just(userName)
        every { authenticationRepository.authenticationCompleted() } returns Observable.never()

        val viewModel = UserAccountsViewModel(
            errorMessageHandler = mockk(),
            investorProductsRepository = investorProductsRepository,
            userRepository = userRepository,
            authenticationRepository = authenticationRepository,
            schedulerProvider = TestSchedulerProvider()
        )

        // assert
        viewModel.getUiState()
            .test()
            .assertValueAt(0, ProductsUIState.Content(productsContent))
            .dispose()
    }

    @Test
    fun investor_products_error_is_handled_correctly() {
        // arrange
        val errorBody = ErrorBody("", "", emptyList())
        val investorProductsRepository: InvestorProductsRepository = mockk()
        val userRepository: UserRepository = mockk()
        val authenticationRepository: AuthenticationRepository = mockk()
        val authenticationSubject = PublishSubject.create<Boolean>()

        every { investorProductsRepository.getInvestorProducts() } returns Observable.never()
        every { investorProductsRepository.loadInvestorProducts() } returns Single.just(Response.Error(errorBody))
        every { userRepository.getUserName() } returns Observable.never()
        every { authenticationRepository.authenticationCompleted() } returns authenticationSubject

        val viewModel = UserAccountsViewModel(
            errorMessageHandler = mockk(),
            investorProductsRepository = investorProductsRepository,
            userRepository = userRepository,
            authenticationRepository = authenticationRepository,
            schedulerProvider = TestSchedulerProvider()
        )

        val uiStateObserver = viewModel.getUiState().test()

        // act
        authenticationSubject.onNext(true)

        // assert
        uiStateObserver
            .assertValueAt(0, ProductsUIState.Loading)
            .assertValueAt(1, ProductsUIState.Message(R.string.Your_session_expired_please_log_in_again))
            .dispose()
    }
}