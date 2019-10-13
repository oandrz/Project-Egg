/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle

import starbright.com.projectegg.R
import starbright.com.projectegg.features.base.WebviewActivity

class RecipeDetailActivity : BaseActivityWithToolbar(), RecipeDetailFragment.FragmentListener {

    private var mFragment: RecipeDetailFragment? = null

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
                .attach(mFragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.detail_title)
        setBackButtonEnabled(true)
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
                .findFragmentByTag(RECIPE_FRAGMENT_TAG) as? RecipeDetailFragment
        if (mFragment == null) {
            mFragment = RecipeDetailFragment.newInstance(intent
                    .getStringExtra(EXTRA_RECIPE_ID))
            supportFragmentManager.beginTransaction()
                    .add(R.id.contentFrame, mFragment, RECIPE_FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun navigateToWebViewActivity(url: String) {
        startActivity(WebviewActivity.newIntent(this, url))
    }

    companion object {
        private const val RECIPE_FRAGMENT_TAG = "RECIPE_FRAGMENT_TAG"
        private const val EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID"

        fun newIntent(context: Context, recipeId: String): Intent {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra(EXTRA_RECIPE_ID, recipeId)
            return intent
        }
    }
}