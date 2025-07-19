/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.list

import starbright.com.projectegg.databinding.FragmentRecipeHomeBinding
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
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.search.SearchRecipeActivity
import starbright.com.projectegg.view.RecipeHeader
import starbright.com.projectegg.view.RecipeItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.widget.TextView

class RecipeHomeFragment: BaseFragment<RecipeHomeContract.View, RecipeHomePresenter>(), RecipeHomeContract.View {

    private var _binding: FragmentRecipeHomeBinding? = null
    private val binding get() = _binding!!
    
    // Error layout views
    private val layoutError: View by lazy { binding.layoutError.root }
    private val ivFailImage: androidx.appcompat.widget.AppCompatImageView by lazy { 
        binding.root.findViewById(R.id.iv_fail_image)
    }
    private val tvFailTitle: TextView by lazy {
        binding.root.findViewById(R.id.tv_fail_title)
    }
    private val tvFailDescription: TextView by lazy {
        binding.root.findViewById(R.id.tv_fail_description)
    }
    
    // Search view
    private val searchView: View by lazy { binding.search.root }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): RecipeHomeContract.View = this

    override fun setupSearchView() {
        searchView.setOnClickListener {
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

        binding.rvRecipe.run {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
            addOnScrollListener(endlessScrollListener)
        }
        recipeHeaderAdapter.add(listOf(RecipeHeader(getString(R.string.home_list_header))))
    }

    override fun populateList(recipe: List<Recipe>) {
        Handler(Looper.getMainLooper()).post {
            binding.rvRecipe.visibility = View.VISIBLE
            recipeFooterAdpter.clear()
            recipe.map {
                recipeBodyAdapter.add(RecipeItem(it))
            }
        }
    }

    override fun showFooterLoading(recipe: List<Recipe>) {
        Handler(Looper.getMainLooper()).post {
            recipeFooterAdpter.clear()
            recipeFooterAdpter.add(ProgressItem())
        }
    }

    override fun showErrorState() {
        Handler(Looper.getMainLooper()).post {
            recipeFooterAdpter.clear()
            layoutError.visibility = View.VISIBLE
            activity?.let {
                ivFailImage.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_error))
                tvFailTitle.text = getString(R.string.error_title_system)
                tvFailDescription.text = getString(R.string.error_desc_system)
            }
            binding.rvRecipe.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}