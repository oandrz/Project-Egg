/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home

import starbright.com.projectegg.databinding.ActivityHomeBinding
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.features.base.BaseActivity
import javax.inject.Inject

class HomeActivity : BaseActivity<HomeContract.View, HomePresenter>(), HomeContract.View {

    private lateinit var binding: ActivityHomeBinding


    @Inject lateinit var bottomNavigationItemFactory: BottomNavigationFactory

    override fun getLayoutRes(): Int = R.layout.activity_home

    override fun getView(): HomeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)
        
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        // Initialize binding after content view is set
        binding = ActivityHomeBinding.bind((findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
    }

    override fun setupBottomSheet() {
        // Make status bar translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
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
}
