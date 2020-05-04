package starbright.com.projectegg.features.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.scope.ActivityScope
import starbright.com.projectegg.features.home.list.RecipeHomeFragment
import starbright.com.projectegg.features.home.setting.SettingFragment
import javax.inject.Inject

interface BottomNavigationFactory {
    fun create(navigationItemId: Int)
}

@ActivityScope
class HomeBottomNavigationFactory @Inject constructor(
    private val supportFragmentManager: FragmentManager
) : BottomNavigationFactory {

    private var activeFragment: Fragment? = null

    override fun create(navigationItemId: Int) {
        when (navigationItemId) {
            R.id.navigation_home -> {
                addFragment(RecipeHomeFragment.TAG)
            }
            R.id.navigation_setting -> {
                addFragment(SettingFragment.TAG)
            }
            else -> throw IllegalStateException("unknown navigation item, there's no item with that id")
        }
    }

    private fun addFragment(tag: String) {
        when (activeFragment) {
            is RecipeHomeFragment -> if (tag == RecipeHomeFragment.TAG) return
            is SettingFragment -> if (tag == SettingFragment.TAG) return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = when (tag) {
                RecipeHomeFragment.TAG -> RecipeHomeFragment.newInstance()
                SettingFragment.TAG -> SettingFragment.newInstance()
                else -> throw IllegalStateException("unknown navigation type")
            }
            fragmentTransaction.add(R.id.container, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }
}