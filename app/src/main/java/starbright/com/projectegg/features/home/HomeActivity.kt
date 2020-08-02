/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home

import android.content.Context
import android.content.Intent
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.features.base.BaseActivity
import javax.inject.Inject

class HomeActivity : BaseActivity<HomeContract.View, HomePresenter>(), HomeContract.View {

    @Inject lateinit var bottomNavigationItemFactory: BottomNavigationFactory

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getView(): HomeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupBottomSheet() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
        navigation.run {
            setOnNavigationItemSelectedListener { item ->
                bottomNavigationItemFactory.create(item.itemId)
                true
            }
            selectedItemId = R.id.navigation_home
        }
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}
