/**
 * Created by Andreas on 20/1/2019.
 */

package starbright.com.projectegg.features.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import starbright.com.projectegg.R
import starbright.com.projectegg.features.base.BaseActivityWithToolbar

class FilterActivity : BaseActivityWithToolbar() {

    private var fragment: FilterFragment? = null

    override fun onStart() {
        super.onStart()
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                    .attach(it)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.recipelist_filter_label)
        setBackButtonEnabled(true)
        initFragment()
    }

    override fun onDestroy() {
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                    .detach(it)
                .commitAllowingStateLoss()
        }
        super.onDestroy()
    }

    private fun initFragment() {
        fragment = supportFragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG) as? FilterFragment
        if (fragment == null) {
            fragment = FilterFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.contentFrame, fragment as Fragment, FILTER_FRAGMENT_TAG)
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