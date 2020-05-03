/**
 * Created by Andreas on 14/10/2019.
 */

package starbright.com.projectegg.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutRes(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.run {
            attachView(getViewContract())
            onCreateScreen()
        }
    }

    override fun onDestroyView() {
        presenter.onStopScreen()
        super.onDestroyView()
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

    private fun buildFragmentComponent(): FragmentComponent = DaggerFragmentComponent.builder()
        .applicationComponent((context?.applicationContext as MyApp).appComponent)
        .fragmentModule(FragmentModule(this))
        .build()

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)
    protected abstract fun getViewContract(): V
}