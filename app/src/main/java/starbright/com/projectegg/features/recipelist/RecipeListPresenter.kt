/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.data.local.model.Recipe
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider
import java.util.*

internal class RecipeListPresenter(private val mRepository: AppRepository,
                                   private val mView: RecipeListContract.View,
                                   private val mSchedulerProvider: BaseSchedulerProvider) : RecipeListContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable

    private var mRecipes: List<Recipe> = listOf()
    private var mIngredients: List<Ingredient> = listOf()

    init {
        mView.setPresenter(this)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun start() {
        mView.setupRecyclerView()
        mView.setupSwipeRefreshLayout()
        getRecipesBasedIngredients(mapIngredients())
    }

    fun getRecipesBasedIngredients(ingredients: String) {
        mView.showLoadingBar()
        mCompositeDisposable.add(
                mRepository.getRecipes(ingredients)
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe({ recipes ->
                            mRecipes = recipes.toMutableList()
                            mView.hideLoadingBar()
                            mView.bindRecipesToList(recipes.toMutableList())
                        }, { throwable ->
                            mView.hideLoadingBar()
                            mView.showErrorSnackBar(throwable.message ?: "")
                        })
        )
    }

    override fun handleListItemClicked(position: Int) {
        mView.showDetail(mRecipes[position].id.toString())
    }

    override fun handleRefresh() {
        getRecipesBasedIngredients(mapIngredients())
    }

    override fun setIngredients(ingredients: MutableList<Ingredient>) {
        mIngredients = ArrayList(ingredients)
    }

    private fun mapIngredients(): String {
        val stringBuilder = StringBuilder()
        var ingredientSize = mIngredients.size
        for (ingredient in mIngredients) {
            stringBuilder.append(ingredient.name)
            ingredientSize--
            if (ingredientSize > 0) {
                stringBuilder.append(", ")
            }
        }
        return stringBuilder.toString()
    }
}
