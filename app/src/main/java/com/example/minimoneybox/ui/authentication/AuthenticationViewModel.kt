package com.example.minimoneybox.ui.authentication

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.repository.AuthenticationRepository
import com.example.minimoneybox.utils.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    fun getAuthorizationCompleted(): Observable<Boolean> {
        return authenticationRepository.authenticationCompleted()
            .subscribeOn(schedulerProvider.io())
            .filter { it }
    }
}