/**
 * Created by Andreas on 5/10/2019.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_recipe_list.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import java.lang.ref.WeakReference
import java.util.*

class RecipeListActivity : BaseActivity<RecipeListContract.View, RecipeListPresenter>(),
        RecipeListContract.View, RecipeListAdapter.Listener {

    private lateinit var adapter: RecipeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(NormalToolbar(
                WeakReference(this), R.id.toolbar, R.string.recipelist_title
        ))
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_recipe_list

    override fun injectDependencies(activityComponent: ActivityComponent) =
            activityComponent.inject(this)

    override fun getActivity(): RecipeListContract.View = this

    override fun setupView() {
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = RecipeListAdapter(this)
        adapter.mListener = this
        rv_recipe.let {
            it.layoutManager = LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false
            )
            it.adapter = adapter
        }
    }

    override fun onItemClicked(position: Int) {
        presenter.handleListItemClicked(position)
    }

    private fun setupSwipeRefreshLayout() {
        swipe_refresh_container.let {
            it.setColorSchemeColors(ContextCompat.getColor(this, R.color.red))
            it.setOnRefreshListener { presenter.handleRefresh() }
        }
    }

    override fun showLoadingBar() {
        swipe_refresh_container.isRefreshing = true
    }

    override fun hideLoadingBar() {
        swipe_refresh_container.isRefreshing = false
    }

    override fun bindRecipesToList(recipes: MutableList<Recipe>) {
        rv_recipe.visibility = View.VISIBLE
        adapter.setRecipes(recipes)
    }

    override fun provideIngredients(): List<Ingredient>? {
        intent.extras?.getParcelableArrayList<Ingredient>(INGREDIENT_EXTRA_KEY)?.let {
            return it
        }
        return null
    }

    override fun showDetail(recipeId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorSnackBar(errorMessage: String) {
        Snackbar.make(root_layout, errorMessage, Snackbar.LENGTH_SHORT)
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
