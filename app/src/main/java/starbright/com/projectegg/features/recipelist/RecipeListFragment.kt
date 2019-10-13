/**
 * Created by Andreas on 10/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import java.util.*
import javax.inject.Inject

class RecipeListFragment : Fragment(), RecipeListContract.View, RecipeListAdapter.Listener {

    @Inject
    lateinit var schedulerProvider: SchedulerProviderContract
    @Inject
    lateinit var repo: AppRepository

    private var mFragmentListener: FragmentListener? = null
    private lateinit var mPresenter: RecipeListContract.Presenter
    private lateinit var mAdapter: RecipeListAdapter

    override fun onAttach(context: Context?) {
//        MyApp.getAppComponent().inject(this)
        super.onAttach(context)
        mFragmentListener = context as? FragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecipeListPresenter(repo, this, schedulerProvider)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.setIngredients(arguments!!.getParcelableArrayList(INGREDIENT_LIST_BUNDLE)!!)
        mPresenter.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.mListener = null
    }

    override fun onDetach() {
        super.onDetach()
        mFragmentListener = null
    }

    override fun setPresenter(presenter: RecipeListContract.Presenter) {
        mPresenter = presenter
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