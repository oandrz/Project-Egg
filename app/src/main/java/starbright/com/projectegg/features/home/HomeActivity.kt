/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.databinding.ActivityHomeBinding
import starbright.com.projectegg.features.base.BaseActivityRevamped
import javax.inject.Inject

class HomeActivity : BaseActivityRevamped() {

    @Inject lateinit var bottomNavigationItemFactory: BottomNavigationFactory

    private lateinit var binding: ActivityHomeBinding

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun bindActivity() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupBottomSheet() {
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
