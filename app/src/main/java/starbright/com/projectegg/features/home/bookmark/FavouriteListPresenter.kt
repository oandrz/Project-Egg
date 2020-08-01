/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 1 - 8 - 2020.
 */

package starbright.com.projectegg.features.home.bookmark

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class FavouriteListPresenter @Inject constructor(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    schedulerProvider: SchedulerProviderContract,
    private val repository: AppRepository
) : BasePresenter<FavouriteListContract.View>(
    schedulerProvider,
    compositeDisposable,
    networkHelper
), FavouriteListContract.Presenter {

    override fun onCreateScreen() {
        view.setupView()
    }

    override fun handleItemClick(recipeId: Int) {
        view.navigateDetailPage(recipeId)
    }

    override fun getFavouriteList() {
        compositeDisposable.add(
            repository.getFavouriteRecipe()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    if (it.isEmpty()) {
                        view.renderEmptyView()
                    } else {
                        view.renderList(it.map {
                            Recipe(
                                it.recipeId,
                                it.cookingTimeInMinutes,
                                it.servingCount,
                                it.recipeTitle,
                                it.recipeImageUrl,
                                sourceName = it.source
                            )
                        })
                    }
                }, {
                    view.renderEmptyView()
                })
        )
    }
}