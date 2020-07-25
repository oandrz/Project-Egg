/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.features.home.list

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.dagger.scope.FragmentScope
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.mock.mockRecipe
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

@FragmentScope
class RecipeHomePresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
): BasePresenter<RecipeHomeContract.View>(schedulerProvider, compositeDisposable, networkHelper),
    RecipeHomeContract.Presenter {

    override fun onCreateScreen() {
        view.run {
            setupSearchView()
            setupList()
        }
    }

    override fun loadRecipe(page: Int) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)
        compositeDisposable.add(
            repository.getRecommendedRecipe(page)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipeList ->
                    view.populateList(recipeList)
                }, {
                    view.showError(it.toString())
                })
        )
    }

    override fun handleLoadMore(currentPage: Int) {
        view.showFooterLoading(mockRecipe)
        loadRecipe(currentPage)
    }

    override fun handleItemClick(selectedRecipeId: String) {
        view.navigateDetailPage(selectedRecipeId)
    }
}