/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.dagger.component.DaggerActivityComponent
import starbright.com.projectegg.dagger.module.ActivityModule

abstract class BaseActivity<> : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildComponent())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }

    private fun buildComponent(): ActivityComponent = DaggerActivityComponent.builder()
            .applicationComponent((application as MyApp).appComponent)
            .activityModule(ActivityModule(this))
            .build()

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
}
