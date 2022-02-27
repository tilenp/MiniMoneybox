package com.example.minimoneybox.ui.account.user_accounts

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.R
import com.example.minimoneybox.model.local.InvestorProducts
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.UserRepository
import com.example.minimoneybox.utils.ErrorMessageHandler
import com.example.minimoneybox.utils.SchedulerProvider
import com.example.minimoneybox.utils.THROTTLE_INTERVAL
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserAccountsViewModel @Inject constructor(
    private val errorMessageHandler: ErrorMessageHandler,
    private val investorProductsRepository: InvestorProductsRepository,
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val schedulerProvider: SchedulerProvider
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val uiStateObserver = BehaviorSubject.create<ProductsUIState>().toSerialized()
    private val userActionDispatcher = PublishSubject.create<UserAction>()
    private val userActionObserver = PublishSubject.create<ProductsUIState>()

    init {
        observeProductsContent()
        loadInvestorProducts()
        observeProductClick()
    }

    private fun observeProductsContent() {
        compositeDisposable.add(
            Observable.combineLatest(
                userRepository.getUserName(),
                investorProductsRepository.getInvestorProducts()
            ) { userName, investorProducts -> updateContent(userName, investorProducts) }
                .map { ProductsUIState.Content(it) }
                .subscribe({ uiStateObserver.onNext(it) }, {})
        )
    }

    private fun loadInvestorProducts() {
        compositeDisposable.add(
            Observable.merge(
                authenticationRepository.authenticationCompleted(),
                userActionDispatcher.map { it is UserAction.OnRetryClick }
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .filter { it }
                .switchMap {
                    investorProductsRepository.loadInvestorProducts()
                        .toObservable()
                        .filter { it is Response.Error }
                        .map { ProductsUIState.Message(R.string.Your_session_expired_please_log_in_again) as ProductsUIState }
                        .startWithItem(ProductsUIState.Loading)
                        .onErrorResumeNext { Observable.just(mapInvestorProductsError(it)) } }
                .subscribe({ uiStateObserver.onNext(it) }, {})
        )
    }

    private fun observeProductClick() {
        compositeDisposable.add(
            userActionDispatcher
                .throttleFirst(THROTTLE_INTERVAL, TimeUnit.MILLISECONDS, schedulerProvider.interval())
                .filter { it is UserAction.OnProductClick }
                .map { (it as UserAction.OnProductClick).productId }
                .flatMapSingle { investorProductsRepository.selectProduct(it)
                    .andThen(Single.just(R.id.action_userAccountsFragment_to_individualAccountFragment)) }
                .subscribe({ userActionObserver.onNext(ProductsUIState.Navigate(it)) },{})
        )
    }

    private fun updateContent(userName: String, investorProducts: InvestorProducts): ProductsContent {
        return ProductsContent(
            userName = userName,
            totalPlanValue = investorProducts.totalPlanValue.formattedAmount,
            products = investorProducts.products
        )
    }

    private fun mapInvestorProductsError(throwable: Throwable): ProductsUIState {
        val messageId = errorMessageHandler.getExceptionMessage(throwable)
        return ProductsUIState.Retry(messageId)
    }

    fun getUiState(): Observable<ProductsUIState> {
        return Observable.merge(
            uiStateObserver,
            userActionObserver
        )
    }

    fun onProductClick(productId: Long?) {
        productId?.let { userActionDispatcher.onNext(UserAction.OnProductClick(it)) }
    }

    fun onRetryClick() {
        userActionDispatcher.onNext(UserAction.OnRetryClick)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private sealed class UserAction {
        data class OnProductClick(val productId: Long): UserAction()
        object OnRetryClick: UserAction()
    }
}