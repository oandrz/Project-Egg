/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.dagger.component.DaggerActivityComponent
import starbright.com.projectegg.dagger.module.ActivityModule
import javax.inject.Inject

abstract class BaseActivity<V : BaseViewContract, P : BasePresenter<V>> : AppCompatActivity(), BaseViewContract {

    @Inject
    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildComponent())
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        presenter.run {
            attachView(getActivity())
            onCreateScreen()
        }
    }

    override fun onDestroy() {
        presenter.onDestroyScreen()
        super.onDestroy()
    }

    private fun buildComponent(): ActivityComponent = DaggerActivityComponent.builder()
            .applicationComponent((application as MyApp).appComponent)
            .activityModule(ActivityModule(this))
            .build()

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

    override fun navigateToHome() {
        ///todo: intent to home
    }

    open fun goBack() = onBackPressed()

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun getActivity(): V

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
}
