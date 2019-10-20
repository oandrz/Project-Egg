/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import java.util.*
import javax.inject.Inject

class RecipeListPresenter @Inject constructor(
        schedulerProvider: SchedulerProviderContract,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val mRepository: AppRepository
) : BasePresenter<RecipeListContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeListContract.Presenter {
    override fun onCreateScreen() {
        view.setupRecyclerView()
        view.setupSwipeRefreshLayout()
        getRecipesBasedIngredients(mapIngredients())
    }

    private var mRecipes: List<Recipe> = listOf()
    private var mIngredients: List<Ingredient> = listOf()


    fun getRecipesBasedIngredients(ingredients: String) {
        view.showLoadingBar()
        compositeDisposable.add(
                mRepository.getRecipes(ingredients)
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .subscribe({ recipes ->
                            mRecipes = recipes.toMutableList()
                            view.hideLoadingBar()
                            view.bindRecipesToList(recipes.toMutableList())
                        }, { throwable ->
                            view.hideLoadingBar()
                            view.showErrorSnackBar(throwable.message ?: "")
                        })
        )
    }

    override fun handleListItemClicked(position: Int) {
        view.showDetail(mRecipes[position].id.toString())
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
