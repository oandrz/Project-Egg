/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home

import android.content.Context
import android.content.Intent
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.databinding.ActivityHomeBinding
import starbright.com.projectegg.features.base.BaseActivity
import javax.inject.Inject

class HomeActivity : BaseActivity<HomeContract.View, HomePresenter>(), HomeContract.View {

    @Inject lateinit var bottomNavigationItemFactory: BottomNavigationFactory

    private lateinit var binding: ActivityHomeBinding

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getView(): HomeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupBottomSheet() {
        binding.navigation.run {
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

    override fun bindActivity() {
       binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
