package com.example.minimoneybox.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.dagger.MyViewModelFactory
import com.example.minimoneybox.dagger.ViewModelKey
import com.example.minimoneybox.ui.authentication.AuthenticationViewModel
import com.example.minimoneybox.ui.authentication.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: MyViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    fun bindAuthenticationViewModel(viewModel: AuthenticationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}
