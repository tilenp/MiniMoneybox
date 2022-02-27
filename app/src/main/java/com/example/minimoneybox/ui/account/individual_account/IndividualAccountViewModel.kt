package com.example.minimoneybox.ui.account.individual_account

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.R
import com.example.minimoneybox.model.local.Moneybox
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.utils.ErrorMessageHandler
import com.example.minimoneybox.utils.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class IndividualAccountViewModel @Inject constructor(
    private val investorProductsRepository: InvestorProductsRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorMessageHandler: ErrorMessageHandler,
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val uiStateSubject = BehaviorSubject.create<IndividualAccountUIState>().toSerialized()
    private val userActionDispatcher = PublishSubject.create<UserAction>()

    init {
        observeOnOffPaymentContent()
        setUpOneOffPayments()
    }

    private fun observeOnOffPaymentContent() {
        compositeDisposable.add(
            investorProductsRepository.getSelectedProduct()
                .map { IndividualAccountContent(it) }
                .map { IndividualAccountUIState.Content(it) }
                .subscribe({ uiStateSubject.onNext(it) },{})
        )
    }

    private fun setUpOneOffPayments() {
        compositeDisposable.add(
            userActionDispatcher
                .filter { it is UserAction.AddAmount }
                .map { (it as UserAction.AddAmount).amount }
                .flatMap { amount ->
                    investorProductsRepository.addAmount(amount)
                        .subscribeOn(schedulerProvider.io())
                        .toObservable()
                        .map { mapOneOffPaymentsResponse(it) }
                        .startWithItem(IndividualAccountUIState.Loading)
                        .concatWith(Observable.just(IndividualAccountUIState.NotLoading))
                        .onErrorResumeNext { Observable.just(mapOneOffPaymentsError(it)) }
                }
                .subscribe({ uiStateSubject.onNext(it) },{})
        )
    }

    private fun mapOneOffPaymentsResponse(response: Response<Moneybox, ErrorBody>): IndividualAccountUIState {
        return when (response) {
            is Response.Success -> IndividualAccountUIState.Message(R.string.Payment_completed)
            is Response.Error -> IndividualAccountUIState.ServerMessage(response.error.Message)
        }
    }

    private fun mapOneOffPaymentsError(throwable: Throwable): IndividualAccountUIState {
        val messageId = errorMessageHandler.getExceptionMessage(throwable)
        return IndividualAccountUIState.Message(messageId)
    }

    fun getUiState(): Observable<IndividualAccountUIState> {
        return uiStateSubject
    }

    fun addAmount() {
        userActionDispatcher.onNext(UserAction.AddAmount(10.0))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private sealed class UserAction {
        data class AddAmount(val amount: Double): UserAction()
    }
}