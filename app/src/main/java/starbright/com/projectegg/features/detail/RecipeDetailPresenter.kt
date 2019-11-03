/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.features.detail

import io.reactivex.disposables.CompositeDisposable
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
        if (mRecipe != null && mRecipe!!.sourceStringUrl != null) {
            view.createShareIntent(mRecipe!!.sourceStringUrl!!, mRecipe!!.title)
        } else {
            //todo: show toast
        }
    }

    override fun handleWebViewMenuClicked() {
        if (mRecipe != null && mRecipe!!.sourceStringUrl != null) {
            view.navigateToWebViewActivity(mRecipe!!.sourceStringUrl!!)
        } else {
            //todo: show toast
        }
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
