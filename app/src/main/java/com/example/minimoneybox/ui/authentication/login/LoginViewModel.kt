package com.example.minimoneybox.ui.authentication.login

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.Response
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.repository.UserRepository
import com.example.minimoneybox.utils.ErrorMessageHandler
import com.example.minimoneybox.utils.SchedulerProvider
import com.example.minimoneybox.ui.authentication.AuthenticationUIState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val errorMessageHandler: ErrorMessageHandler,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val uiStateObserver = BehaviorSubject.create<AuthenticationUIState>()
    private val userActionDispatcher = PublishSubject.create<UserAction>()

    init {
        setUpLoginObservable()
    }

    private fun setUpLoginObservable() {
        compositeDisposable.add(
            userActionDispatcher
                .observeOn(schedulerProvider.io())
                .filter { it is UserAction.Login }
                .map { (it as UserAction.Login).data }
                .switchMap { authorizationData ->
                    loginRepository.login(authorizationData)
                        .toObservable()
                        .map { mapLoginStatus(it) }
                        .startWithItem(AuthenticationUIState.Loading)
                        .onErrorResumeNext { Observable.just(mapLoginError(it)) }
                        .concatWith(userRepository.setUserName(authorizationData.name)
                            .andThen(Observable.just(AuthenticationUIState.NotLoading))
                        )
                }
                .subscribe({ uiStateObserver.onNext(it) }, {})
        )
    }

    private fun mapLoginStatus(response: Response<Session, ErrorBody>): AuthenticationUIState {
        return when (response) {
            is Response.Success -> AuthenticationUIState.NotLoading
            is Response.Error -> AuthenticationUIState.Error(response.error)
        }
    }

    private fun mapLoginError(throwable: Throwable): AuthenticationUIState {
        val messageId = errorMessageHandler.getExceptionMessage(throwable)
        return AuthenticationUIState.Message(messageId)
    }

    fun login(email: String, password: String, name: String) {
        val data = AuthorizationData(email.trim(), password.trim(), name.trim())
        userActionDispatcher.onNext(UserAction.Login(data))
    }

    fun observeUIState(): Observable<AuthenticationUIState> {
        return uiStateObserver
    }

    companion object {
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
    }

    private sealed class UserAction {
        data class Login(val data: AuthorizationData): UserAction()
    }
}