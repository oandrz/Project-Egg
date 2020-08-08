/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 8 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.layout_error_state.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment
import starbright.com.projectegg.features.detail.RecipeDetailActivity
import starbright.com.projectegg.view.RecipeItem

class FavouriteListFragment : BaseFragment<FavouriteListContract.View, FavouriteListPresenter>(),
    FavouriteListContract.View {

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
        Handler().post {
            rv_favourite.visibility = View.VISIBLE
            layout_error.visibility = View.GONE
            recipeBodyAdapter.setNewList(favouriteRecipes.map {
                RecipeItem(it)
            })
        }
    }

    override fun renderEmptyView() {
        rv_favourite.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
        activity?.let {
            iv_fail_image.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_empty_box))
        }
        tv_fail_title.text = getString(R.string.error_title_empty_favorite)
        tv_fail_description.text = getString(R.string.error_desc_empty_favorite)
    }

    private fun setupToolbar() {
        with((activity as AppCompatActivity)) {
            setSupportActionBar(toolbar)
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
        rv_favourite.apply {
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