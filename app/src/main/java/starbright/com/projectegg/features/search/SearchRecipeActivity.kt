/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.features.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_search_recipe.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.ActivityComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseActivity
import starbright.com.projectegg.features.base.NormalToolbar
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.ingredients.IngredientsActivity
import starbright.com.projectegg.features.recipelist.RecipeListActivity
import starbright.com.projectegg.view.RecentSearchItem
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.SearchSuggestionItem
import java.lang.ref.WeakReference

class SearchRecipeActivity : BaseActivity<SearchRecipeContract.View, SearchRecipePresenter>(),
    SearchRecipeContract.View {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbarBehavior(
            NormalToolbar(WeakReference(this), R.id.toolbar, R.string.recipelist_title)
        )
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadSearchHistory()
    }

    override fun getLayoutRes(): Int = R.layout.activity_search_recipe

    override fun getView(): SearchRecipeContract.View = this

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_recipe, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as? SearchView
        searchView?.run {
            setIconifiedByDefault(true)
            isIconified = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    presenter.handleUserSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    presenter.handleUserTypeInEditText(newText)
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun setupView() {
        fab_search_ingredient.setOnClickListener {
            startActivity(IngredientsActivity.newIntent(this))
        }
        setupList()
    }

    override fun showLoading() {
        view_loading.visibility = View.VISIBLE
        rv_search_history.visibility = View.GONE
    }

    override fun hideLoading() {
        view_loading.visibility = View.GONE
    }

    override fun renderRecipeSuggestion(recipes: List<Recipe>) {
        Handler().post {
            historySearchHeader.clear()
            recipeSuggestionItem.clear()
            historySearchItem.clear()
            recipes.map {
                recipeSuggestionItem.add(SearchSuggestionItem(it))
            }
        }
    }

    override fun renderSearchHistory(searchQueries: List<String>) {
        Handler().post {
            historySearchHeader.clear()
            recipeSuggestionItem.clear()
            if (searchQueries.isNotEmpty()) {
                historySearchHeader.add(RecipeHeader(getString(R.string.search_header_history_label)))
                val items = searchQueries.map { query ->
                    RecentSearchItem(query) { selectedQuery, position ->
                        presenter.handleRemoveSearchHistory(selectedQuery, position)
                    }
                }
                historySearchItem.set(items)
            }
        }
    }

    override fun navigateRecipeList(query: String) {
        startActivity(
            RecipeListActivity.newIntent(
                this@SearchRecipeActivity,
                query = query
            )
        )
    }

    override fun navigateRecipeDetail(recipeId: String) {
        startActivity(
            RecipeDetailActivity.getIntent(
                this@SearchRecipeActivity,
                recipeId
            )
        )
    }

    override fun removeSelectedSearchQuery(position: Int) {
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
                    presenter.handleSuggestionItemClicked(item.recipe.id.toString())
                } else if (item is RecentSearchItem) {
                    presenter.handleRecentSearchItemClicked(item.text)
                }
                true
            }
        }
        rv_search_history?.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SearchRecipeActivity::class.java)
    }
}
