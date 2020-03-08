/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class RecipeListPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val mRepository: AppRepository
) : BasePresenter<RecipeListContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeListContract.Presenter {

    private val dataSource: MutableList<Recipe> = mutableListOf()
    private var lastOffset: Int = -1
    private var ingredients: List<Ingredient> = listOf()

    override fun onCreateScreen() {
        view.let {
            ingredients = it.provideIngredients() ?: listOf()
            it.setupView()
        }
        refresh(mapIngredients())
    }

    override fun handleListItemClicked(position: Int) {
        view.showDetail(dataSource[position].id.toString())
    }

    override fun handleRefresh() {
        refresh(mapIngredients())
    }

    override fun handleLoadMore() {
        lastOffset++
        compositeDisposable.add(
            mRepository.getRecipes(mapIngredients(), lastOffset)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipesResult ->
                    dataSource.addAll(recipesResult)
                    view.appendRecipes(recipesResult, recipesResult.isNotEmpty())
                }, { throwable ->
                    view.showErrorSnackBar(throwable.message ?: "")
                })
        )
    }

    override fun setIngredients(ingredients: MutableList<Ingredient>) {
        this.ingredients = ingredients
    }

    private fun refresh(ingredients: String) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)
        view.showLoadingBar()
        lastOffset = 0
        compositeDisposable.add(
            mRepository.getRecipes(ingredients, lastOffset)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipesResult ->
                    dataSource.clear()
                    dataSource.addAll(recipesResult)
                    view.hideLoadingBar()
                    view.bindRecipesToList(recipesResult)
                }, { throwable ->
                    view.hideLoadingBar()
                    view.showErrorSnackBar(
                        "Our services are under maintenance, please retry after some time"
                    )
                })
        )
    }

    private fun mapIngredients(): String {
        val stringBuilder = StringBuilder()
        var ingredientSize = ingredients.size
        for (ingredient in ingredients) {
            stringBuilder.append(ingredient.name)
            ingredientSize--
            if (ingredientSize > 0) {
                stringBuilder.append(", ")
            }
        }
        return stringBuilder.toString()
    }
}
