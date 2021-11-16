/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.databinding.FragmentRecipeHomeBinding
import starbright.com.projectegg.features.base.BaseFragmentRevamped
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.home.list.RecipeHomeViewModel.RecipeHomeFragmentState.*
import starbright.com.projectegg.features.search.SearchRecipeActivity
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.RecipeItem
import javax.inject.Inject

class RecipeHomeFragment: BaseFragmentRevamped() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecipeHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecipeHomeViewModel::class.java)
    }

    private var binding: FragmentRecipeHomeBinding? = null

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    private val recipeHeaderAdapter: ItemAdapter<RecipeHeader> by lazy {
        ItemAdapter<RecipeHeader>()
    }

    private val recipeFooterAdapter: ItemAdapter<ProgressItem> by lazy {
        ItemAdapter<ProgressItem>()
    }

    private val endlessScrollListener: EndlessRecyclerOnScrollListener =
        object : EndlessRecyclerOnScrollListener(recipeFooterAdapter) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.handleLoadMore(currentPage)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecipeHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupSearchView()
        setupObserver()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    private fun setupObserver() {
        viewModel.viewStateLive.observe(viewLifecycleOwner) {
            when(it) {
                is RenderList -> { populateList(it.recipe) }
                is ErrorState -> { showErrorState() }
                is RenderFooterLoad -> { showFooterLoading() }
                is NavigateDetailPage -> { navigateDetailPage(it.selectedRecipeId) }
            }
        }
    }

    private fun setupSearchView() {
        binding?.search?.root?.setOnClickListener {
            activity?.let {
                startActivity(SearchRecipeActivity.newIntent(it))
            }
        }
    }

    private fun setupList() {
        val fastAdapter = FastAdapter.with(
            listOf(recipeHeaderAdapter, recipeBodyAdapter, recipeFooterAdapter)
        ).apply {
            onClickListener =  { view, _, item, _ ->
                if (view != null && item is RecipeItem) {
                    viewModel.handleItemClick(item.recipe.id.toString())
                }
                false
            }
        }

        binding?.rvRecipe?.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
            addOnScrollListener(endlessScrollListener)
        }
        recipeHeaderAdapter.add(listOf(RecipeHeader(getString(R.string.home_list_header))))
    }

    private fun populateList(recipe: List<Recipe>) {
        binding?.rvRecipe?.visibility = View.VISIBLE
        recipeFooterAdapter.clear()
        recipe.map {
            recipeBodyAdapter.add(RecipeItem(it))
        }
    }

    private fun showFooterLoading() {
        if (!recipeFooterAdapter.itemList.isEmpty) {
            recipeFooterAdapter.clear()
        }
        recipeFooterAdapter.add(ProgressItem())
    }

    private fun showErrorState() {
        recipeFooterAdapter.clear()
        binding?.layoutError?.root?.visibility = View.VISIBLE
        binding?.rvRecipe?.visibility = View.GONE
    }

    private fun navigateDetailPage(recipeId: String) {
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