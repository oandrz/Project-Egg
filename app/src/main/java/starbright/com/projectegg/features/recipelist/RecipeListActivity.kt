/**
 * Created by Andreas on 5/10/2019.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import starbright.com.projectegg.R
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.features.base.BaseActivityWithToolbar
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import java.util.*

class RecipeListActivity : BaseActivityWithToolbar(), RecipeListFragment.FragmentListener {

    private var mFragment: RecipeListFragment? = null

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
                .attach(mFragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.recipelist_title)
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
        val ingredients = intent.extras?.getParcelableArrayList<Ingredient>(INGREDIENT_EXTRA_KEY)
        mFragment = supportFragmentManager
                .findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG) as? RecipeListFragment
        if (mFragment == null) {
            ingredients?.let {
                mFragment = RecipeListFragment.newInstance(it)
                supportFragmentManager.beginTransaction()
                        .add(R.id.contentFrame, mFragment, RECIPE_LIST_FRAGMENT_TAG)
                        .commit()
            }
        }
    }

    override fun navigateRecipeDetailActivity(recipeId: String) {
        startActivity(RecipeDetailActivity.newIntent(this, recipeId))
    }

    companion object {

        private const val RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAGMENT_TAG"
        private const val INGREDIENT_EXTRA_KEY = "INGREDIENT_EXTRA_KEY"

        fun newIntent(context: Context, ingredients: List<Ingredient>): Intent {
            val intent = Intent(context, RecipeListActivity::class.java)
            intent.putExtra(INGREDIENT_EXTRA_KEY, ArrayList(ingredients))
            return intent
        }
    }
}
