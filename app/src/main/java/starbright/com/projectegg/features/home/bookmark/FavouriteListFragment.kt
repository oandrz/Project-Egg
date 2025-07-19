/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import starbright.com.projectegg.databinding.FragmentFavouriteBinding
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.view.RecipeItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class FavouriteListFragment : BaseFragment<FavouriteListContract.View, FavouriteListPresenter>(),
    FavouriteListContract.View {

    private var _binding: FragmentFavouriteBinding? = null
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

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeBodyAdapter: ItemAdapter<RecipeItem> by lazy {
        ItemAdapter<RecipeItem>()
    }

    override fun onResume() {
        super.onResume()
        presenter.getFavouriteList()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_favourite
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): FavouriteListContract.View = this

    override fun setupView() {
        setupToolbar()
        setupList()
    }

    override fun navigateDetailPage(id: Int) {
        activity?.apply {
            startActivity(RecipeDetailActivity.getIntent(this, id.toString()))
        }
    }

    override fun renderList(favouriteRecipes: List<Recipe>) {
        Handler(Looper.getMainLooper()).post {
            binding.rvFavourite.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
            recipeBodyAdapter.setNewList(favouriteRecipes.map {
                RecipeItem(it)
            })
        }
    }

    override fun renderEmptyView() {
        binding.rvFavourite.visibility = View.GONE
        layoutError.visibility = View.VISIBLE
        activity?.let {
            ivFailImage.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_empty_box))
        }
        tvFailTitle.text = getString(R.string.error_title_empty_favorite)
        tvFailDescription.text = getString(R.string.error_desc_empty_favorite)
    }

    private fun setupToolbar() {
        with((activity as AppCompatActivity)) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = resources.getString(R.string.favourite_text_title_toolbar)
        }
    }

    private fun setupList() {
        val fastAdapter = FastAdapter.with(listOf(recipeBodyAdapter)).apply {
            onClickListener = { view, _, item, _ ->
                if (view != null) {
                    presenter.handleItemClick(item.recipe.id)
                }
                false
            }
        }
        binding.rvFavourite.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = linearLayoutManager
            adapter = fastAdapter
        }
    }

    companion object {
        const val TAG = "Favourite"
        fun newInstance(): FavouriteListFragment = FavouriteListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}