/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.layout_error_state.*
import kotlinx.android.synthetic.main.widget_floating_filter_sort.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.SortOption
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.base.UNKNOWN_RESOURCE
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.recipelist.recipefilter.RecipeFilterBottomSheetFragment
import starbright.com.projectegg.features.recipelist.recipesort.RecipeSortBottomSheetFragment
import starbright.com.projectegg.view.RecipeItem
import java.lang.ref.WeakReference

class RecipeListActivity : BaseActivity<RecipeListContract.View, RecipeListPresenter>(),
    RecipeListContract.View {

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    private val recipeFooterAdapter: ItemAdapter<ProgressItem> by lazy {
        ItemAdapter<ProgressItem>()
    }

    private val endlessScrollListener: EndlessRecyclerOnScrollListener =
        object : EndlessRecyclerOnScrollListener(recipeFooterAdapter) {
            override fun onLoadMore(currentPage: Int) {
                presenter.handleLoadMore(recipeBodyAdapter.adapterItemCount)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(NormalToolbar(
            WeakReference(this), R.id.toolbar,
            if (intent.extras?.getString(QUERY_EXTRA_KEY) == null) {
                UNKNOWN_RESOURCE
            } else {
                R.string.recipelist_title
            }
        ))
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_recipe_list

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun getView(): RecipeListContract.View = this

    override fun setupView() {
        setupRecyclerView()
        tv_sort.setOnClickListener {
            presenter.handleSortActionClicked()
        }

        tv_filter.setOnClickListener {
            presenter.handleFilterActionClicked()
        }
    }

    override fun showFooterLoading() {
        Handler().post {
            recipeFooterAdapter.apply {
                clear()
                add(ProgressItem())
            }
        }
    }

    override fun appendRecipes(recipes: List<Recipe>) {
        Handler().post {
            rv_recipe.visibility = View.VISIBLE
            recipeFooterAdapter.clear()
            recipes.map {
                recipeBodyAdapter.add(RecipeItem(it))
            }
        }
    }

    override fun provideSearchConfig(): RecipeConfig {
        return RecipeConfig(
            intent.extras?.getString(QUERY_EXTRA_KEY),
            null,
            ingredients = intent.extras?.getParcelableArrayList<Ingredient>(INGREDIENT_EXTRA_KEY)
        )
    }

    override fun showDetail(recipeId: String) {
        startActivity(RecipeDetailActivity.getIntent(this, recipeId))
    }

    override fun showFilterBottomSheet(
        cuisines: List<String>,
        selectedCuisine: String?
    ) {
        RecipeFilterBottomSheetFragment()
            .also {
                it.cuisines = cuisines
                it.selectedCuisine = selectedCuisine
                it.onBottomSheetDismissListener = { cuisine ->
                    presenter.handleFilterItemSelected(cuisine)
                }
            }.show(supportFragmentManager, "cartbot")
    }

    override fun showSortBottomSheet(sortOption: ArrayList<SortOption>, selectedSortOption: String) {
        RecipeSortBottomSheetFragment.newInstance(sortOption, selectedSortOption).apply {
            listener = { selectedSort ->
                presenter.handleSortItemSelected(
                    RecipeSortCategory.values().first { selectedSort == it.type }
                )
            }
        }.show(supportFragmentManager, "sort")
    }

    override fun clearRecipe() {
        recipeBodyAdapter.clear()
        recipeFooterAdapter.clear()
        endlessScrollListener.resetPageCount()
    }

    override fun hideFilterButton() {
        fab_sort_filter.visibility = View.GONE
    }

    override fun showFilterButton() {
        fab_sort_filter.visibility = View.VISIBLE
    }

    override fun showResultEmptyState() {
        rv_recipe.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
        iv_fail_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_empty_box))
        tv_fail_title.text = getString(R.string.error_title_empty_recipe)
        tv_fail_description.text = getString(R.string.error_desc_empty_recipe)
    }

    override fun showErrorState() {
        rv_recipe.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
        iv_fail_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_error))
        tv_fail_title.text = getString(R.string.error_title_system)
        tv_fail_description.text = getString(R.string.error_desc_system)
    }

    override fun hideFooterLoading() {
        Handler().post {
            recipeFooterAdapter.clear()
        }
    }

    private fun setupRecyclerView() {
        val fastAdapter = FastAdapter.with(listOf(recipeBodyAdapter, recipeFooterAdapter)).apply {
            onClickListener =  { view, _, item, _ ->
                if (view != null && item is RecipeItem) {
                    presenter.handleListItemClicked(item.recipe.id.toString())
                }
                false
            }
        }

        rv_recipe.run {
            layoutManager = LinearLayoutManager(
                this@RecipeListActivity, LinearLayoutManager.VERTICAL, false
            )
            itemAnimator = DefaultItemAnimator()
            adapter = fastAdapter
            addOnScrollListener(endlessScrollListener)
        }
    }

    companion object {
        private const val INGREDIENT_EXTRA_KEY = "INGREDIENT_EXTRA_KEY"
        private const val QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY"

        fun newIntent(context: Context, ingredients: List<Ingredient>? = null, query: String? = null): Intent {
            return Intent(context, RecipeListActivity::class.java).also {
                ingredients?.let { ingredients ->
                    it.putExtra(INGREDIENT_EXTRA_KEY, ArrayList(ingredients))
                }
                it.putExtra(QUERY_EXTRA_KEY, query)
            }
        }
    }
}
