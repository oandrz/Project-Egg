/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeListContract {

    interface View : BaseViewContract {
        fun setupView()
        fun provideIngredients(): List<Ingredient>?

        fun showLoadingBar()
        fun hideLoadingBar()

        fun bindRecipesToList(recipes: List<Recipe>)
        fun appendRecipes(recipes: List<Recipe>, hasNext: Boolean)
        fun showDetail(recipeId: String)

        fun showErrorSnackBar(errorMessage: String)
    }

    interface Presenter : BasePresenterContract {
        fun handleListItemClicked(position: Int)
        fun handleRefresh()
        fun handleLoadMore(lastPosition: Int)

        fun setIngredients(ingredients: MutableList<Ingredient>)
    }
}
