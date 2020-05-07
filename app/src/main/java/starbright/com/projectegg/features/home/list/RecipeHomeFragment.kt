package starbright.com.projectegg.features.home.list

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_recipe_home.*
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.component.FragmentComponent
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BaseFragment

class RecipeHomeFragment: BaseFragment<RecipeHomeContract.View, RecipeHomePresenter>(), RecipeHomeContract.View {

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val recipeListAdapter: RecipeHomeAdapter by lazy {
        RecipeHomeAdapter()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_recipe_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun getViewContract(): RecipeHomeContract.View = this

    companion object {
        const val TAG = "Recipe Home"
        fun newInstance(): RecipeHomeFragment {
            return RecipeHomeFragment()
        }
    }

    override fun setupSearchView() {
        search.setOnClickListener {

        }
    }

    override fun setupList() {
        rv_recipe.run {
            layoutManager = linearLayoutManager
            adapter = recipeListAdapter
        }
    }

    override fun populateList(recipe: MutableList<Recipe>) {
        recipeListAdapter.addAll(recipe)
    }
}