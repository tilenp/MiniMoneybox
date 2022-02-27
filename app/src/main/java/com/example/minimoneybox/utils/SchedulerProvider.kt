package com.example.minimoneybox.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun main(): Scheduler
    fun interval(): Scheduler
}

class RuntimeSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun interval(): Scheduler {
        return Schedulers.computation()
    }
}

class TestSchedulerProvider(private val testScheduler: TestScheduler = TestScheduler()) :
    SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun main(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun interval(): Scheduler {
        return testScheduler
    }
}