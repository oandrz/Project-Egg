/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg

import android.app.Application
import android.os.Build
import starbright.com.projectegg.dagger.component.ApplicationComponent
import starbright.com.projectegg.dagger.component.DaggerApplicationComponent
import starbright.com.projectegg.dagger.module.AppModule

class MyApp : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        val isPreLolipop: Boolean
            get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    }
}
