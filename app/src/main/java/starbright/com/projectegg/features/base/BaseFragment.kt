/**
 * Created by Andreas on 14/10/2019.
 */

package starbright.com.projectegg.features.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.DaggerFragmentComponent
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.dagger.module.FragmentModule
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewContract, P : BasePresenter<V>> : Fragment(), BaseViewContract {

    @Inject
    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
    }

    private fun buildFragmentComponent(): FragmentComponent = DaggerFragmentComponent.builder()
            .applicationComponent((context?.applicationContext as MyApp).appComponent)
            .fragmentModule(FragmentModule(this))
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayoutRes(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun showError(res: Int?, text: String?) {
        if (res != null) {
            Toast.makeText(activity, res, Toast.LENGTH_SHORT).show()
        } else if (text != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateToHome() {
        if (activity is BaseActivity<*, *>) (activity as BaseActivity<*, *>).navigateToHome()
    }

    fun goBack() {
        if (activity is BaseActivity<*, *>) (activity as BaseActivity<*, *>).goBack()
    }

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)
    protected abstract fun setupView(view: View)
}