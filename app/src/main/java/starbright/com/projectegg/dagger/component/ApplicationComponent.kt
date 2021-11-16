/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.dagger.component

import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import starbright.com.projectegg.dagger.module.*
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.local.database.ApplicationDatabase
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    StorageModule::class,
    DataStoreModule::class,
])
interface ApplicationComponent {
    fun getSchedulerProvider(): SchedulerProviderContract
    fun getCompositeDiposable(): CompositeDisposable
    fun getNetworkHelper(): NetworkHelper
    fun getAppRepository(): AppRepository
    fun getClient(): OkHttpClient
    fun getDatabase(): ApplicationDatabase
}
