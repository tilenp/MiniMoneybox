package com.example.minimoneybox.dagger

import android.app.Application

class MoneyBoxApplication : Application(), AppComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun provideAppComponent(): AppComponent {
        return appComponent
    }
}