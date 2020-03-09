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
import kotlinx.android.synthetic.main.activity_recipe_list.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.detail.RecipeDetailActivity
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

    override fun getView(): RecipeListContract.View = this

    override fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = RecipeListAdapter()
        adapter.listener = this
        rv_recipe.let {
            it.setLayoutManager(LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
            ))
            it.adapter = adapter
            it.setRefreshListener {
                presenter.handleRefresh()
            }
            it.setRefreshingColor(
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.red)
            )
            it.setNumberBeforeMoreIsCalled(1)
            it.setOnMoreListener { _, _, lastItemPos ->
                presenter.handleLoadMore(lastItemPos)
            }
        }
    }

    override fun onItemClicked(position: Int) {
        presenter.handleListItemClicked(position)
    }

    override fun showLoadingBar() {
        rv_recipe.apply {
            setRefreshing(true)
            showProgress()
        }
    }

    override fun hideLoadingBar() {
        rv_recipe.apply {
            setRefreshing(false)
            hideProgress()
        }
    }

    override fun bindRecipesToList(recipes: List<Recipe>) {
        adapter.refreshItems(recipes)
        rv_recipe.showRecycler()
    }

    override fun appendRecipes(recipes: List<Recipe>, hasNext: Boolean) {
        adapter.addAll(recipes)
        if (!hasNext) {
            rv_recipe.isLoadingMore = true
        }
    }

    override fun provideIngredients(): List<Ingredient>? {
        intent.extras?.getParcelableArrayList<Ingredient>(INGREDIENT_EXTRA_KEY)?.let {
            return it
        }
        return null
    }

    override fun showDetail(recipeId: String) {
        startActivity(RecipeDetailActivity.getIntent(this, recipeId))
    }

    override fun showErrorSnackBar(errorMessage: String) {
        Snackbar.make(root_layout, errorMessage, Snackbar.LENGTH_SHORT)
    }

    companion object {
        private const val INGREDIENT_EXTRA_KEY = "INGREDIENT_EXTRA_KEY"

        fun newIntent(context: Context, ingredients: List<Ingredient>): Intent {
            val intent = Intent(context, RecipeListActivity::class.java)
            intent.putExtra(INGREDIENT_EXTRA_KEY, ArrayList(ingredients))
            return intent
        }
    }
}
