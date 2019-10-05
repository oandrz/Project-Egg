/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.features.detail

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.local.model.Recipe
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider

class RecipeDetailPresenter(
        private val mRepository: AppRepository,
        private val mView: RecipeDetailContract.View,
        private val mSchedulerProvider: BaseSchedulerProvider
) : RecipeDetailContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable

    private lateinit var mRecipeId: String
    private var mRecipe: Recipe? = null

    init {
        mView.setPresenter(this)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun start() {
        mView.setupSwipeRefreshLayout()
        getRecipeDetailInformation(mRecipeId)
    }

    override fun setRecipeId(recipeId: String) {
        mRecipeId = recipeId
    }

    override fun getRecipeDetailInformation(recipeId: String) {
        mView.apply {
            hideScrollContainer()
            showProgressBar()
        }
        mCompositeDisposable
                .add(mRepository.getRecipeDetailInformation(recipeId)
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe({ recipe ->
                            mView.hideProgressBar()
                            mRecipe = recipe
                            if (mRecipe != null) {
                                mView.hideEmptyStateTextView()
                                updateView()
                            } else {
                                mView.renderEmptyStateTextView()
                            }
                        }, {
                            mView.hideProgressBar()
                            if (mRecipe != null) {
                                mView.hideEmptyStateTextView()
                                updateView()
                            } else {
                                mView.renderEmptyStateTextView()
                            }
                        }))
    }

    override fun handleShareMenuClicked() {
        if (mRecipe != null && mRecipe!!.sourceStringUrl != null) {
            mView.createShareIntent(mRecipe!!.sourceStringUrl!!, mRecipe!!.title)
        } else {
            //todo: show toast
        }
    }

    override fun handleWebViewMenuClicked() {
        if (mRecipe != null && mRecipe!!.sourceStringUrl != null) {
            mView.navigateToWebViewActivity(mRecipe!!.sourceStringUrl!!)
        } else {
            //todo: show toast
        }
    }

    private fun updateView() {
        mRecipe?.let { recipe ->
            with(mView) {
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
