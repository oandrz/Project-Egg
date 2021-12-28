/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 9 - 8 - 2020.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
import starbright.com.projectegg.databinding.ActivityRecipeListBinding
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.BaseActivityRevamped
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.base.UNKNOWN_RESOURCE
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.recipelist.RecipeListViewModel.RecipeListState.*
import starbright.com.projectegg.features.recipelist.recipefilter.RecipeFilterBottomSheetFragment
import starbright.com.projectegg.features.recipelist.recipesort.RecipeSortBottomSheetFragment
import starbright.com.projectegg.features.search.SearchRecipeViewModel
import starbright.com.projectegg.view.RecipeItem
import java.lang.ref.WeakReference
import javax.inject.Inject

class RecipeListActivity : BaseActivityRevamped() {

    private lateinit var binding: ActivityRecipeListBinding

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    private val recipeFooterAdapter: ItemAdapter<ProgressItem> by lazy {
        ItemAdapter<ProgressItem>()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecipeListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RecipeListViewModel::class.java]
    }

    private val endlessScrollListener: EndlessRecyclerOnScrollListener =
        object : EndlessRecyclerOnScrollListener(recipeFooterAdapter) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.handleLoadMore(recipeBodyAdapter.adapterItemCount)
            }
        }

    override fun bindActivity() {
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

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
        setupView()
        observeViewModel()
        viewModel.init(provideSearchConfig())
    }

    private fun setupView() {
        setupRecyclerView()
        binding.fabSortFilter.run {
            tvSort.setOnClickListener {
                viewModel.handleSortActionClicked()
            }

            tvFilter.setOnClickListener {
                viewModel.handleFilterActionClicked()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) {
            when(it) {
                is NavigateRecipeDetail -> showDetail(it.recipeId)
                is ShowErrorMessage -> showError(it.resId)
                is RenderErrorState -> showErrorState()
                is SuccessEmptyRecipeState -> showResultEmptyState()
                is SuccessLoadRecipe -> handleSuccessLoadRecipe(it.recipeList)
                is ShowSortSelector -> showSortBottomSheet(ArrayList(it.sortOption), it.selectedSort)
                is ShowFilterSelector -> showFilterBottomSheet(it.filterOption, it.selectedFilter)
                is ResetList -> resetList()
                is ShowFooterLoading -> showFooterLoading()
                is HideFooterLoading -> hideFooterLoading()
            }
        }
    }

    private fun handleSuccessLoadRecipe(recipes: List<Recipe>) {
        if (recipes.first().totalRecipe < 11) {
            disableLoadMore()
        }
        showFilterButton()
        appendRecipes(recipes)
    }

    private fun resetList() {
        clearRecipe()
        showFooterLoading()
    }

    private fun showFooterLoading() {
        recipeFooterAdapter.run {
            clear()
            add(ProgressItem())
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        binding.rvRecipe.visibility = View.VISIBLE
        recipeFooterAdapter.clear()
        recipes.map {
            recipeBodyAdapter.add(RecipeItem(it))
        }
    }

    private fun provideSearchConfig(): RecipeConfig {
        return RecipeConfig(
            intent.extras?.getString(QUERY_EXTRA_KEY),
            null,
            ingredients = intent.extras?.getParcelableArrayList<Ingredient>(INGREDIENT_EXTRA_KEY)
        )
    }

    private fun showDetail(recipeId: String) {
        startActivity(RecipeDetailActivity.getIntent(this, recipeId))
    }

    private fun showFilterBottomSheet(
        cuisines: List<String>,
        selectedCuisine: String?
    ) {
        RecipeFilterBottomSheetFragment()
            .also {
                it.cuisines = cuisines
                it.selectedCuisine = selectedCuisine
                it.onBottomSheetDismissListener = { cuisine ->
                    viewModel.handleFilterItemSelected(cuisine)
                }
            }.show(supportFragmentManager, "cartbot")
    }

    private fun showSortBottomSheet(sortOption: ArrayList<SortOption>, selectedSortOption: String?) {
        RecipeSortBottomSheetFragment.newInstance(sortOption, selectedSortOption).apply {
            listener = { selectedSort ->
                viewModel.handleSortItemSelected(
                    RecipeSortCategory.values().first { selectedSort == it.type }
                )
            }
        }.show(supportFragmentManager, "sort")
    }

    private fun clearRecipe() {
        recipeBodyAdapter.clear()
        recipeFooterAdapter.clear()
        endlessScrollListener.resetPageCount()
    }

    private fun hideFilterButton() {
        binding.fabSortFilter.root.visibility = View.GONE
    }

    private fun showFilterButton() {
        binding.fabSortFilter.root.visibility = View.VISIBLE
    }

    private fun showResultEmptyState() {
        hideFilterButton()
        binding.rvRecipe.visibility = View.GONE
        binding.layoutError.run {
            root.visibility = View.VISIBLE
            ivFailImage.setImageDrawable(ContextCompat.getDrawable(this@RecipeListActivity, R.drawable.ic_empty_box))
            tvFailTitle.text = getString(R.string.error_title_empty_recipe)
            tvFailDescription.text = getString(R.string.error_desc_empty_recipe)
        }
    }

    private fun showErrorState() {
        binding.rvRecipe.visibility = View.GONE
        binding.layoutError.run {
            root.visibility = View.VISIBLE
            ivFailImage.setImageDrawable(ContextCompat.getDrawable(this@RecipeListActivity, R.drawable.ic_error))
            tvFailTitle.text = getString(R.string.error_title_system)
            tvFailDescription.text = getString(R.string.error_desc_system)
        }
    }

    private fun disableLoadMore() {
        binding.rvRecipe.clearOnScrollListeners()
    }

    private fun hideFooterLoading() {
        recipeFooterAdapter.clear()
    }

    private fun setupRecyclerView() {
        val fastAdapter = FastAdapter.with(listOf(recipeBodyAdapter, recipeFooterAdapter)).apply {
            onClickListener = { view, _, item, _ ->
                if (view != null && item is RecipeItem) {
                    viewModel.handleListItemClicked(item.recipe.id.toString())
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
