/**
 * Created by Andreas on 20/1/2019.
 */

package starbright.com.projectegg.features.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import starbright.com.projectegg.R
import starbright.com.projectegg.features.base.BaseActivityWithToolbar

class FilterActivity : BaseActivityWithToolbar() {

    private var fragment: FilterFragment? = null

    override fun onStart() {
        super.onStart()
        supportFragmentManager
                .beginTransaction()
                .attach(fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.recipelist_filter_label)
        setBackButtonEnabled(true)
        initFragment()
    }

    override fun onDestroy() {
        supportFragmentManager
                .beginTransaction()
                .detach(fragment)
                .commitAllowingStateLoss()
        super.onDestroy()
    }

    private fun initFragment() {
        fragment = supportFragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG) as? FilterFragment
        if (fragment == null) {
            fragment = FilterFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.contentFrame, fragment, FILTER_FRAGMENT_TAG)
                    .commit()
        }
    }

    companion object {
        private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG"

        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, FilterActivity::class.java)
        }
    }
}