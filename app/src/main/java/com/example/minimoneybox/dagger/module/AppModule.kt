package com.example.minimoneybox.dagger.module

import com.example.minimoneybox.utils.RuntimeSchedulerProvider
import com.example.minimoneybox.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return RuntimeSchedulerProvider()
    }
}