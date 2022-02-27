package com.example.minimoneybox.dagger

import android.content.Context
import com.example.minimoneybox.dagger.module.*
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
        ServiceModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}