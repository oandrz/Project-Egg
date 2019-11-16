/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast

import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.dagger.component.DaggerActivityComponent
import starbright.com.projectegg.dagger.module.ActivityModule
import javax.inject.Inject

abstract class BaseActivity<V : BaseViewContract, P : BasePresenter<V>> : AppCompatActivity(), BaseViewContract {

    @Inject
    lateinit var presenter: P

    private var toolbarBehavior: ToolbarBehavior? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildComponent())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        setToolbarIfExist()
        presenter.run {
            attachView(getView())
            onCreateScreen()
        }
    }

    private fun buildComponent(): ActivityComponent = DaggerActivityComponent.builder()
        .applicationComponent((application as MyApp).appComponent)
        .activityModule(ActivityModule(this))
        .build()

    private fun setToolbarIfExist() {
        toolbarBehavior?.buildToolbar()
    }

    override fun onDestroy() {
        presenter.onStopScreen()
        super.onDestroy()
    }

    override fun showError(res: Int?, text: String?) {
        if (res != null) {
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
        } else if (text != null) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun navigateToHome() {
        ///todo: intent to home
    }

    protected fun setToolbarBehavior(behavior: ToolbarBehavior) {
        toolbarBehavior = behavior
    }

    open fun goBack() = onBackPressed()

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun getView(): V

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
}
