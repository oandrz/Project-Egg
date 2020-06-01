/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class RecipeListPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val repository: AppRepository
) : BasePresenter<RecipeListContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeListContract.Presenter {

    private var cuisines: MutableSet<String> = mutableSetOf()
    private lateinit var firstVersionConfig: RecipeConfig
    private lateinit var config: RecipeConfig

    override fun onCreateScreen() {
        view.run {
            config = provideSearchConfig()
            setupView()
            showFooter()
        }
        loadRecipe(0)
    }

    override fun handleListItemClicked(selectedRecipeId: String) {
        view.showDetail(selectedRecipeId)
    }

    override fun handleRefresh() {
        config = firstVersionConfig
        loadRecipe(0)
    }

    override fun handleLoadMore(lastPosition: Int) {
        view.showFooter()
        loadRecipe(lastPosition)
    }

    override fun handleSortActionClicked() {

    }

    override fun handleSortItemSelected(sortType: String) {
        TODO("Not yet implemented")
    }

    override fun handleFilterActionClicked() {
        view.showFilterBottomSheet(cuisines.toList(), config.cuisine)
    }

    override fun handleFilterItemSelected(cuisine: String) {
        config = firstVersionConfig
        config.cuisine = cuisine
        loadRecipe(0)
    }

    private fun loadRecipe(pos: Int) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)
        compositeDisposable.add(
            repository.getRecipes(config, pos)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipesResult ->
                    recipesResult.forEach { it.cuisines?.mapTo(cuisines) { cuisine -> cuisine } }
                    view.appendRecipes(recipesResult)
                }, { _ ->
                    view.run {
                        hideFooterLoading()
                        showError(text = "Our services are under maintenance, please retry after some time")
                    }
                })
        )
    }
}
