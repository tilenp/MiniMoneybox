package com.example.minimoneybox.ui.account.individual_account

import com.example.minimoneybox.R
import com.example.minimoneybox.model.local.Amount
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.local.Product
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.utils.TestSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class IndividualAccountViewModelTest {

    @Test
    fun content_state_is_correct() {
        // arrange
        val product = Product(1)
        val amount = Amount(10.0, "Add £10")
        val content = IndividualAccountContent(product, amount)

        val investorProductsRepository: InvestorProductsRepository = mockk()
        every { investorProductsRepository.getSelectedProduct() } returns Observable.just(product)

        val viewModel = IndividualAccountViewModel(
            errorMessageHandler = mockk(),
            investorProductsRepository = investorProductsRepository,
            schedulerProvider = TestSchedulerProvider()
        )

        // assert
        viewModel.getUiState()
            .test()
            .assertValue(IndividualAccountUIState.Content(content))
            .dispose()
    }

    @Test
    fun test_one_off_payment_success() {
        // arrange
        val investorProductsRepository: InvestorProductsRepository = mockk()
        every { investorProductsRepository.getSelectedProduct() } returns Observable.never()
        every { investorProductsRepository.addAmount(any()) } returns Single.just(Response.Success(Moneybox()))

        val viewModel = IndividualAccountViewModel(
            errorMessageHandler = mockk(),
            investorProductsRepository = investorProductsRepository,
            schedulerProvider = TestSchedulerProvider()
        )

        val uiStateObserver = viewModel.getUiState().test()

        // act
        viewModel.addAmount()

        // assert
        uiStateObserver
            .assertValueAt(0, IndividualAccountUIState.Loading)
            .assertValueAt(1, IndividualAccountUIState.Message(R.string.Payment_completed))
            .assertValueAt(2, IndividualAccountUIState.NotLoading)
            .dispose()
    }

    @Test
    fun test_one_off_payment_server_error() {
        // arrange
        val serverMessage = "server message"
        val errorBody = ErrorBody("", serverMessage, emptyList())

        val investorProductsRepository: InvestorProductsRepository = mockk()
        every { investorProductsRepository.getSelectedProduct() } returns Observable.never()
        every { investorProductsRepository.addAmount(any()) } returns Single.just(Response.Error(errorBody))

        val viewModel = IndividualAccountViewModel(
            errorMessageHandler = mockk(),
            investorProductsRepository = investorProductsRepository,
            schedulerProvider = TestSchedulerProvider()
        )

        val uiStateObserver = viewModel.getUiState().test()

        // act
        viewModel.addAmount()

        // assert
        uiStateObserver
            .assertValueAt(0, IndividualAccountUIState.Loading)
            .assertValueAt(1, IndividualAccountUIState.ServerMessage(serverMessage))
            .assertValueAt(2, IndividualAccountUIState.NotLoading)
            .dispose()
    }
}