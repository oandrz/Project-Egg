/**
 * Created by Andreas on 11/8/2019.
 */

/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import android.content.Context
import android.content.Intent
import android.os.Bundle

import starbright.com.projectegg.R
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.features.recipelist.RecipeListActivity
import starbright.com.projectegg.features.userAccount.UserAccountActivity

class IngredientsActivity : BaseActivityWithoutToolbar(), IngredientsFragment.FragmentListener {

    private var mFragment: IngredientsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .attach(mFragment)
            .commit()
    }

    override fun onDestroy() {
        supportFragmentManager.beginTransaction()
            .detach(mFragment)
            .commitAllowingStateLoss()
        super.onDestroy()
    }

    private fun initFragment() {
        mFragment = supportFragmentManager
            .findFragmentByTag(INGREDIENTS_FRAGMENT_TAG) as? IngredientsFragment?
        if (mFragment == null) {
            mFragment = IngredientsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.contentFrame, mFragment, INGREDIENTS_FRAGMENT_TAG)
                .commit()
        }
    }

    override fun navigateUserAccountActivity() {
        startActivity(UserAccountActivity.newIntent(this))
        finish()
    }

    override fun navigateRecipeListActivity(ingredients: MutableList<Ingredient>) {
        startActivity(RecipeListActivity.newIntent(this, ingredients))
    }

    companion object {

        private const val INGREDIENTS_FRAGMENT_TAG = "INGREDIENTS_FRAGMENT_TAG"

        fun newIntent(context: Context): Intent {
            return Intent(context, IngredientsActivity::class.java)
        }
    }
}
