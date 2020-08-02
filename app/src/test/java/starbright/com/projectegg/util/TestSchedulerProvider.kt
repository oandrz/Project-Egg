/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 17/11/2019.
 */

package starbright.com.projectegg.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProviderContract {
    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler
}