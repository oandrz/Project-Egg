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
                        view.hideEmptyStateTextView()
                        updateView()
                    } else {
                        view.renderEmptyStateTextView()
                    }
                }, {
                    view.hideProgressBar()
                    if (mRecipe != null) {
                        view.hideEmptyStateTextView()
                        updateView()
                    } else {
                        view.renderEmptyStateTextView()
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
                renderBannerFoodImage(recipe.image)
                renderHeaderContainer(recipe.servingCount, recipe.preparationMinutes,
                    recipe.cookingMinutes, recipe.title)
                recipe.ingredients?.let { ingredients ->
                    renderIngredientCard(ingredients)
                }
                recipe.instructions?.let { instructions ->
                    renderInstructionCard(instructions)
                }
            }
        }
    }
}
