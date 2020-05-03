package starbright.com.projectegg.features.home

import android.content.Context
import android.content.Intent
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.home.list.RecipeHomeFragment

class HomeActivity : BaseActivity<HomeContract.View, HomePresenter>(), HomeContract.View {

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getView(): HomeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupBottomSheet() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
        navigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, RecipeHomeFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.navigation_setting -> {
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.navigation_home
        }
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}
