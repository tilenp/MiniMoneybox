package com.example.minimoneybox.dagger

import android.content.Context
import com.example.minimoneybox.dagger.module.*
import com.example.minimoneybox.ui.authentication.AuthenticationActivity
import com.example.minimoneybox.ui.authentication.login.LoginFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        CacheModule::class,
        MapperModule::class,
        RepositoryModule::class,
        ServiceModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: AuthenticationActivity)

    fun inject(fragment: LoginFragment)
}