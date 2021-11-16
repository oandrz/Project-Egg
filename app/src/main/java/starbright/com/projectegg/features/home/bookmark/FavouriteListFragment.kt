/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.databinding.FragmentFavouriteBinding
import starbright.com.projectegg.features.base.BaseFragmentRevamped
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.features.home.bookmark.FavoriteListViewModel.FavoriteListState.*
import starbright.com.projectegg.view.RecipeItem
import javax.inject.Inject

class FavouriteListFragment : BaseFragmentRevamped() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoriteListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavoriteListViewModel::class.java]
    }

    private var binding: FragmentFavouriteBinding? = null

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.favoriteListState.observe(viewLifecycleOwner) {
            when(it) {
                is RenderList -> {
                    renderList(it.recipes)
                }
                RenderEmptyView -> renderEmptyView()
                RenderError -> renderEmptyView()
                is NavigateDetail -> navigateDetailPage(it.recipeId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteList()
    }

    private fun navigateDetailPage(id: Int) {
        activity?.apply {
            startActivity(RecipeDetailActivity.getIntent(this, id.toString()))
        }
    }

    private fun renderList(favouriteRecipes: List<Recipe>) {
        binding?.run {
            rvFavourite.visibility = View.VISIBLE
            layoutError.root.visibility = View.GONE
            recipeBodyAdapter.setNewList(favouriteRecipes.map { RecipeItem(it) })
        }
    }

    private fun renderEmptyView() {
        binding?.rvFavourite?.visibility = View.GONE
        binding?.layoutError?.run {
            root.visibility = View.VISIBLE
            ivFailImage.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_empty_box) })
            tvFailTitle.text = getString(R.string.error_title_empty_favorite)
            tvFailDescription.text = getString(R.string.error_desc_empty_favorite)
        }
    }

    private fun setupToolbar() {
        with((activity as AppCompatActivity)) {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.title = resources.getString(R.string.favourite_text_title_toolbar)
        }
    }

    private fun setupList() {
        val fastAdapter = FastAdapter.with(listOf(recipeBodyAdapter)).apply {
            onClickListener = { view, _, item, _ ->
                if (view != null) {
                    viewModel.handleItemClick(item.recipe.id)
                }
                false
            }
        }
        binding?.rvFavourite?.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
    }

    companion object {
        const val TAG = "Favourite"
        fun newInstance(): FavouriteListFragment = FavouriteListFragment()
    }
}