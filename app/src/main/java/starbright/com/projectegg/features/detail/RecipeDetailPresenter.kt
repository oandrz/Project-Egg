/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
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

    private var isBookmarked: Boolean = false
    private var recipe: Recipe? = null

    override fun onCreateScreen() {
        view.setupSwipeRefreshLayout()
    }

    override fun getRecipeDetailInformation(recipeId: String) {
        isRecipeBookmarked(recipeId)
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)

        view.run {
            hideScrollContainer()
            showProgressBar()
        }
        compositeDisposable.add(
            repository.getRecipeDetailInformation(recipeId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipe ->
                    view.hideProgressBar()
                    this.recipe = recipe
                    if (this.recipe != null) {
                        view.hideEmptyView()
                        updateView()
                    } else {
                        view.renderEmptyView()
                    }
                }, {
                    view.hideProgressBar()
                    if (recipe != null) {
                        view.hideEmptyView()
                        updateView()
                    } else {
                        view.renderEmptyView()
                    }
                })
        )
    }

    override fun handleShareMenuClicked() {
        recipe?.let {
            val sourceUrl = it.sourceStringUrl
            if (sourceUrl != null) {
                view.createShareIntent(sourceUrl, it.title)
            } else {
                view.showError(R.string.detail_empty_label)
            }
        } ?: view.showError(R.string.detail_empty_label)
    }

    override fun handleWebViewMenuClicked() {
        recipe?.sourceStringUrl?.let {
            view.navigateToWebViewActivity(it)
        } ?: view.showError(R.string.detail_empty_label)
    }

    override fun handleBookmarkRecipeMenuClicked() {
        if (isBookmarked) {
            removeRecipeBookmark()
        } else {
            bookmarkRecipe()
        }
    }

    private fun updateView() {
        recipe?.let { recipe ->
            with(view) {
                showScrollContainer()
                renderBannerFoodImage(recipe.image ?: "")
                renderHeaderContainer(
                    recipe.servingCount ?: 0,
                    recipe.cookingMinutes ?: 0,
                    recipe.title,
                    if (recipe.dishTypes?.isEmpty() == true) "" else recipe.dishTypes?.first()
                        ?: "",
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

    private fun bookmarkRecipe() {
        recipe?.let {
            compositeDisposable.add(
                repository.saveFavouriteRecipe(it)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe {
                        isBookmarked = true
                        view.showSnackbar(R.string.detail_message_favourite_add)
                        view.updateMenu(isBookmarked)
                    }
            )
        }
    }

    private fun removeRecipeBookmark() {
        recipe?.let {
            compositeDisposable.add(
                repository.removeFavouriteRecipe(it.id)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe {
                        isBookmarked = false
                        view.showSnackbar(R.string.detail_message_favourite_remove)
                        view.updateMenu(isBookmarked)
                    }
            )
        }
    }

    private fun isRecipeBookmarked(recipeId: String) {
        compositeDisposable.add(
            repository.isRecipeSavedBefore(recipeId.toInt())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                    isBookmarked = it != null
                    view.updateMenu(isBookmarked)
                }
        )
    }
}
