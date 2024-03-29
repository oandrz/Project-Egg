/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg

import android.app.Application
import android.os.Build
import starbright.com.projectegg.dagger.component.ApplicationComponent
import starbright.com.projectegg.dagger.component.DaggerApplicationComponent
import starbright.com.projectegg.dagger.module.AppModule
import timber.log.Timber

class MyApp : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        val isPreLolipop: Boolean
            get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    }
}
