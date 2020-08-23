/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.list

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import kotlinx.android.synthetic.main.fragment_recipe_home.*
import kotlinx.android.synthetic.main.layout_error_state.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.search.SearchRecipeActivity
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.RecipeItem

class RecipeHomeFragment: BaseFragment<RecipeHomeContract.View, RecipeHomePresenter>(), RecipeHomeContract.View {

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    private val recipeHeaderAdapter: ItemAdapter<RecipeHeader> by lazy {
        ItemAdapter<RecipeHeader>()
    }

    private val recipeFooterAdpter: ItemAdapter<ProgressItem> by lazy {
        ItemAdapter<ProgressItem>()
    }

    private val endlessScrollListener: EndlessRecyclerOnScrollListener =
        object : EndlessRecyclerOnScrollListener(recipeFooterAdpter) {
            override fun onLoadMore(currentPage: Int) {
                presenter.handleLoadMore(currentPage)
            }
        }

    override fun getLayoutRes(): Int = R.layout.fragment_recipe_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): RecipeHomeContract.View = this

    override fun setupSearchView() {
        search.setOnClickListener {
            activity?.let {
                startActivity(SearchRecipeActivity.newIntent(it))
            }
        }
    }

    override fun setupList() {
        val fastAdapter = FastAdapter.with(
            listOf(recipeHeaderAdapter, recipeBodyAdapter, recipeFooterAdpter)
        ).apply {
            onClickListener =  { view, _, item, _ ->
                if (view != null && item is RecipeItem) {
                    presenter.handleItemClick(item.recipe.id.toString())
                }
                false
            }
        }

        rv_recipe.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
            addOnScrollListener(endlessScrollListener)
        }
        recipeHeaderAdapter.add(listOf(RecipeHeader(getString(R.string.home_list_header))))
    }

    override fun populateList(recipe: List<Recipe>) {
        Handler().post {
            rv_recipe.visibility = View.VISIBLE
            recipeFooterAdpter.clear()
            recipe.map {
                recipeBodyAdapter.add(RecipeItem(it))
            }
        }
    }

    override fun showFooterLoading(recipe: List<Recipe>) {
        Handler().post {
            recipeFooterAdpter.clear()
            recipeFooterAdpter.add(ProgressItem())
        }
    }

    override fun showErrorState() {
        Handler().post {
            recipeFooterAdpter.clear()
            layout_error.visibility = View.VISIBLE
            activity?.let {
                iv_fail_image.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_error))
                tv_fail_title.text = getString(R.string.error_title_system)
                tv_fail_description.text = getString(R.string.error_desc_system)
            }
            rv_recipe.visibility = View.GONE
        }
    }

    override fun navigateDetailPage(recipeId: String) {
        activity?.apply {
            startActivity(RecipeDetailActivity.getIntent(this, recipeId))
        }
    }

    companion object {
        const val TAG = "Recipe Home"
        fun newInstance(): RecipeHomeFragment {
            return RecipeHomeFragment()
        }
    }
}