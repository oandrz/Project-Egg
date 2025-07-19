/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 9 - 8 - 2020.
 */

package starbright.com.projectegg.features.recipelist

import starbright.com.projectegg.databinding.ActivityRecipeListBinding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
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
import starbright.com.projectegg.view.SelectorItem
import java.lang.ref.WeakReference
import android.view.ViewGroup
import android.widget.TextView

class RecipeListActivity : BaseActivity<RecipeListContract.View, RecipeListPresenter>(),
    RecipeListContract.View {

    private lateinit var binding: ActivityRecipeListBinding
    
    // Error layout views
    private val layoutError: android.view.View by lazy { binding.layoutError.root }
    private val ivFailImage: androidx.appcompat.widget.AppCompatImageView by lazy { 
        binding.root.findViewById(R.id.iv_fail_image)
    }
    private val tvFailTitle: TextView by lazy {
        binding.root.findViewById(R.id.tv_fail_title)
    }
    private val tvFailDescription: TextView by lazy {
        binding.root.findViewById(R.id.tv_fail_description)
    }

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
    
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        binding = ActivityRecipeListBinding.bind((findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
    }

    override fun getLayoutRes(): Int = R.layout.activity_recipe_list

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun getView(): RecipeListContract.View = this

    override fun setupView() {
        setupRecyclerView()
        binding.fabSortFilter.tvSort.setOnClickListener {
            presenter.handleSortActionClicked()
        }

        binding.fabSortFilter.tvFilter.setOnClickListener {
            presenter.handleFilterActionClicked()
        }
    }

    override fun showFooterLoading() {
        Handler(Looper.getMainLooper()).post {
            recipeFooterAdapter.apply {
                clear()
                add(ProgressItem())
            }
        }
    }

    override fun appendRecipes(recipes: List<Recipe>) {
        Handler(Looper.getMainLooper()).post {
            binding.rvRecipe.visibility = android.view.View.VISIBLE
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
        binding.fabSortFilter.root.visibility = android.view.View.GONE
    }

    override fun showFilterButton() {
        binding.fabSortFilter.root.visibility = android.view.View.VISIBLE
    }

    private fun hideErrorState() {
        binding.rvRecipe.visibility = android.view.View.VISIBLE
        layoutError.visibility = android.view.View.GONE
    }

    private fun showSortAndFilter() {
        binding.fabSortFilter.root.visibility = android.view.View.VISIBLE
    }

    override fun showResultEmptyState() {
        binding.rvRecipe.visibility = android.view.View.GONE
        layoutError.visibility = android.view.View.VISIBLE
        ivFailImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_empty_box))
        tvFailTitle.text = getString(R.string.error_title_empty_recipe)
        tvFailDescription.text = getString(R.string.error_desc_empty_recipe)
    }

    override fun showErrorState() {
        binding.rvRecipe.visibility = android.view.View.GONE
        layoutError.visibility = android.view.View.VISIBLE
        ivFailImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_error))
        tvFailTitle.text = getString(R.string.error_title_system)
        tvFailDescription.text = getString(R.string.error_desc_system)
    }

    override fun disableLoadMore() {
        binding.rvRecipe.clearOnScrollListeners()
    }

    override fun hideFooterLoading() {
        Handler(Looper.getMainLooper()).post {
            recipeFooterAdapter.clear()
        }
    }

    private fun setupRecyclerView() {
        val fastAdapter = FastAdapter.with(listOf(recipeBodyAdapter, recipeFooterAdapter)).apply {
            onClickListener = { view, _, item, _ ->
                if (view != null && item is RecipeItem) {
                    presenter.handleListItemClicked(item.recipe.id.toString())
                }
                false
            }
        }

        binding.rvRecipe.run {
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
