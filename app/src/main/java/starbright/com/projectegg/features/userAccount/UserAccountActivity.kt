/**
 * Created by Andreas on 7/10/2019.
 */

package starbright.com.projectegg.features.userAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle

import starbright.com.projectegg.R
import starbright.com.projectegg.features.base.BaseActivityWithoutToolbar
import starbright.com.projectegg.features.ingredients.IngredientsActivity

class UserAccountActivity : BaseActivityWithoutToolbar(), UserAccountFragment.FragmentListener {

    private var mFragment: UserAccountFragment? = null

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
                .attach(mFragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    override fun onDestroy() {
        supportFragmentManager.beginTransaction()
                .detach(mFragment)
                .commitAllowingStateLoss()
        super.onDestroy()
    }

    private fun initFragment() {
        mFragment = supportFragmentManager
                .findFragmentByTag(LOGIN_FRAGMENT_TAG) as UserAccountFragment
        if (mFragment == null) {
            mFragment = UserAccountFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.contentFrame, mFragment, LOGIN_FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun navigateToIngredientsActivity() {
        startActivity(IngredientsActivity.newIntent(this))
        finish()
    }

    companion object {

        private const val LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG"

        fun newIntent(context: Context): Intent {
            return Intent(context, UserAccountActivity::class.java)
        }
    }
}
