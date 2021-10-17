package starbright.com.projectegg.features.base

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.dagger.component.DaggerActivityComponent
import starbright.com.projectegg.dagger.module.ActivityModule

abstract class BaseActivityRevamped : AppCompatActivity() {

    private var toolbarBehavior: ToolbarBehavior? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildComponent())
        super.onCreate(savedInstanceState)
        bindActivity()
        setToolbarIfExist()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    protected fun setToolbarBehavior(behavior: ToolbarBehavior) {
        toolbarBehavior = behavior
    }

    open fun goBack() = onBackPressed()

    fun showError(res: Int?, text: String?) {
        if (res != null) {
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
        } else if (text != null) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildComponent(): ActivityComponent = DaggerActivityComponent.builder()
        .applicationComponent((application as MyApp).appComponent)
        .activityModule(ActivityModule(supportFragmentManager))
        .build()

    private fun setToolbarIfExist() {
        toolbarBehavior?.buildToolbar()
    }

    protected abstract fun bindActivity()

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
}