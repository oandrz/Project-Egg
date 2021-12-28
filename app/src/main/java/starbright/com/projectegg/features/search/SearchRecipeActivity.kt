/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.features.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.databinding.ActivitySearchRecipeBinding
import starbright.com.projectegg.features.base.BaseActivityRevamped
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.ingredients.IngredientsActivity
import starbright.com.projectegg.features.recipelist.RecipeListActivity
import starbright.com.projectegg.features.search.SearchRecipeViewModel.SearchState.*
import starbright.com.projectegg.view.RecentSearchItem
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.SearchSuggestionItem
import java.lang.ref.WeakReference
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SearchRecipeActivity : BaseActivityRevamped() {

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeSuggestionItem: ItemAdapter<SearchSuggestionItem> by lazy {
        ItemAdapter<SearchSuggestionItem>()
    }

    private val historySearchItem: ItemAdapter<RecentSearchItem> by lazy {
        ItemAdapter<RecentSearchItem>()
    }

    private val historySearchHeader: ItemAdapter<RecipeHeader> by lazy {
        ItemAdapter<RecipeHeader>()
    }

    private val binding: ActivitySearchRecipeBinding by lazy {
        ActivitySearchRecipeBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchRecipeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchRecipeViewModel::class.java]
    }

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(NormalToolbar(WeakReference(this), R.id.toolbar, R.string.recipelist_title))
        super.onCreate(savedInstanceState)
        setupObserver()
        setupView()
        viewModel.observeSuggestedRecipe()
    }

    override fun bindActivity() {
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSearchHistory()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_recipe, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as? SearchView
        searchView?.run {
            setIconifiedByDefault(true)
            isIconified = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.handleUserSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.handleUserTypeInEditText(newText)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupObserver() {
        viewModel.searchRecipeState.observe(this) {
            when(it) {
                is ShowLoading -> showLoading()
                is CloseLoading -> hideLoading()
                is RenderSearchHistory -> renderSearchHistory(it.history)
                is RenderRecipe -> renderRecipeSuggestion(it.recipes)
                is NavigateRecipeList -> navigateRecipeList(it.query)
                is NavigateRecipeDetail -> navigateRecipeDetail(it.recipeId)
                is RenderError -> showError(it.error)
                is UpdateList -> removeSelectedSearchQuery(it.position)
            }
        }
    }

    private fun setupView() {
        binding.fabSearchIngredient.setOnClickListener {
            startActivity(IngredientsActivity.newIntent(this))
        }
        setupList()
    }

    private fun showLoading() {
        binding.apply {
            viewLoading.visibility = View.VISIBLE
            rvSearchHistory.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.viewLoading.visibility = View.GONE
    }

    private fun renderRecipeSuggestion(recipes: List<Recipe>) {
        historySearchHeader.clear()
        recipeSuggestionItem.clear()
        historySearchItem.clear()
        recipeSuggestionItem.set(recipes.map { SearchSuggestionItem(it) })
    }

    private fun renderSearchHistory(searchQueries: List<String>) {
        historySearchHeader.clear()
        recipeSuggestionItem.clear()
        if (searchQueries.isNotEmpty()) {
            historySearchHeader.add(RecipeHeader(getString(R.string.search_header_history_label)))
            val items = searchQueries.map { query ->
                RecentSearchItem(query) { selectedQuery, position ->
                    viewModel.handleRemoveSearchHistory(selectedQuery, position)
                }
            }
            historySearchItem.setNewList(items)
        }
    }

    private fun navigateRecipeList(query: String) {
        startActivity(
            RecipeListActivity.newIntent(
                this@SearchRecipeActivity,
                query = query
            )
        )
    }

    private fun navigateRecipeDetail(recipeId: String) {
        startActivity(
            RecipeDetailActivity.getIntent(this@SearchRecipeActivity, recipeId)
        )
    }

    private fun removeSelectedSearchQuery(position: Int) {
        historySearchItem.remove(position)
        if (historySearchItem.adapterItemCount == 0) {
            historySearchHeader.clear()
        }
    }

    private fun setupList() {
        val fastAdapter = FastAdapter.with(
            listOf(historySearchHeader, recipeSuggestionItem, historySearchItem)
        ).apply {
            onClickListener = { _, _, item, _ ->
                if (item is SearchSuggestionItem) {
                    viewModel.handleSuggestionItemClicked(item.recipe.id.toString())
                } else if (item is RecentSearchItem) {
                    viewModel.handleRecentSearchItemClicked(item.text)
                }
                true
            }
        }
        binding.rvSearchHistory.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SearchRecipeActivity::class.java)
    }
}
