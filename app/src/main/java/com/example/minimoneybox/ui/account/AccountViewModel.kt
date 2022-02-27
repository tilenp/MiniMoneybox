package com.example.minimoneybox.ui.account

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.repository.AuthenticationRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun authenticationCompleted(): Observable<Boolean> {
        return authenticationRepository.authenticationCompleted()
            .filter { !it }
    }

    fun logOut() {
        compositeDisposable.add(
            authenticationRepository.logOut()
                .subscribe({}, {})
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}