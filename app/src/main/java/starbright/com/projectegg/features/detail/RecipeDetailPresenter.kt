/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 5/10/2019.
 */

package starbright.com.projectegg.features.detail

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class RecipeDetailPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val repository: AppRepository
) : BasePresenter<RecipeDetailContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeDetailContract.Presenter {

    private var mRecipe: Recipe? = null

    override fun onCreateScreen() {
        view.setupSwipeRefreshLayout()
    }

    override fun getRecipeDetailInformation(recipeId: String) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)

        view.run {
            hideScrollContainer()
            showProgressBar()
        }
        compositeDisposable
            .add(repository.getRecipeDetailInformation(recipeId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipe ->
                    view.hideProgressBar()
                    mRecipe = recipe
                    if (mRecipe != null) {
                        view.hideEmptyView()
                        updateView()
                    } else {
                        view.renderEmptyView()
                    }
                }, {
                    view.hideProgressBar()
                    if (mRecipe != null) {
                        view.hideEmptyView()
                        updateView()
                    } else {
                        view.renderEmptyView()
                    }
                }))
    }

    override fun handleShareMenuClicked() {
        mRecipe?.let {
            val sourceUrl = it.sourceStringUrl
            if (sourceUrl != null) {
                view.createShareIntent(sourceUrl, it.title)
            } else {
                view.showError(R.string.detail_empty_label)
            }
        } ?: view.showError(R.string.detail_empty_label)
    }

    override fun handleWebViewMenuClicked() {
        mRecipe?.sourceStringUrl?.let {
            view.navigateToWebViewActivity(it)
        } ?: view.showError(R.string.detail_empty_label)
    }

    private fun updateView() {
        mRecipe?.let { recipe ->
            with(view) {
                showScrollContainer()
                renderBannerFoodImage(recipe.image ?: "")
                renderHeaderContainer(
                    recipe.servingCount ?: 0,
                    recipe.cookingMinutes ?: 0,
                    recipe.title,
                    if (recipe.dishTypes?.isEmpty() == true) "" else recipe.dishTypes?.first() ?: "",
                    recipe.calories ?: 0
                )
                recipe.ingredients?.let { ingredients ->
                    renderIngredientsList(ingredients.toMutableList())
                }
                recipe.instructions?.let { instructions ->
                    renderInstructionsList(instructions.toMutableList())
                }
            }
        }
    }
}
