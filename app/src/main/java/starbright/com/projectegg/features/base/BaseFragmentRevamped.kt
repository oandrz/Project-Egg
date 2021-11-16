package starbright.com.projectegg.features.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.DaggerFragmentComponent
import starbright.com.projectegg.dagger.component.FragmentComponent

abstract class BaseFragmentRevamped : Fragment() {

    private var toolbarBehavior: ToolbarBehavior? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
    }

    fun showError(res: Int?, text: String?) {
        if (res != null) {
            Toast.makeText(activity, res, Toast.LENGTH_SHORT).show()
        } else if (text != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack() {
        if (activity is BaseActivityRevamped) (activity as BaseActivityRevamped).goBack()
    }

    protected fun setToolbarBehavior(behavior: ToolbarBehavior) {
        toolbarBehavior = behavior
    }

    private fun buildFragmentComponent(): FragmentComponent = DaggerFragmentComponent.builder()
        .applicationComponent((context?.applicationContext as MyApp).appComponent)
        .build()

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)
}