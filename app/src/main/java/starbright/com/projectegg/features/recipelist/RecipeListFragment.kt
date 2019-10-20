/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment
import java.util.*

class RecipeListFragment : BaseFragment<RecipeListContract.View, RecipeListPresenter>(), RecipeListContract.View, RecipeListAdapter.Listener {

    private var mFragmentListener: FragmentListener? = null
    private lateinit var mPresenter: RecipeListContract.Presenter
    private lateinit var mAdapter: RecipeListAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mFragmentListener = context as? FragmentListener
    }

    override fun getLayoutRes(): Int = R.layout.fragment_recipe_list

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
            fragmentComponent.inject(this)

    override fun setupView(view: View) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.mListener = null
    }

    override fun onDetach() {
        super.onDetach()
        mFragmentListener = null
    }

    override fun setupRecyclerView() {
        mAdapter = RecipeListAdapter(activity!!)
        mAdapter.mListener = this
        rv_recipe.run {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

    override fun setupSwipeRefreshLayout() {
        swipe_refresh_container.run {
            setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.red))
            setOnRefreshListener { mPresenter.handleRefresh() }
        }
    }

    override fun bindRecipesToList(recipes: MutableList<Recipe>) {
        rv_recipe.visibility = View.VISIBLE
        mAdapter.setRecipes(recipes)
    }

    override fun showLoadingBar() {
        swipe_refresh_container.isRefreshing = true
    }

    override fun hideLoadingBar() {
        swipe_refresh_container.isRefreshing = false
    }

    override fun showDetail(recipeId: String) {
        mFragmentListener!!.navigateRecipeDetailActivity(recipeId)
    }

    override fun showErrorSnackBar(errorMessage: String) {
        Snackbar.make(root_layout, errorMessage, Snackbar.LENGTH_SHORT)
    }

    override fun onItemClicked(position: Int) {
        mPresenter.handleListItemClicked(position)
    }

    internal interface FragmentListener {
        fun navigateRecipeDetailActivity(recipeId: String)
    }

    companion object {

        private const val INGREDIENT_LIST_BUNDLE = "INGREDIENT_LIST_BUNDLE"

        fun newInstance(ingredients: ArrayList<Ingredient>): RecipeListFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(INGREDIENT_LIST_BUNDLE, ingredients)
            val fragment = RecipeListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}