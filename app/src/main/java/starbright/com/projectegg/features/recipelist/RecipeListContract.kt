/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.features.base.BaseView

class RecipeListContract {

    interface View : BaseView<Presenter> {
        fun setupRecyclerView()

        fun setupSwipeRefreshLayout()

        fun showLoadingBar()
        fun hideLoadingBar()

        fun bindRecipesToList(recipes: MutableList<Recipe>)
        fun showDetail(recipeId: String)

        fun showErrorSnackBar(errorMessage: String)
    }

    interface Presenter : BasePresenter {
        fun handleListItemClicked(position: Int)
        fun handleRefresh()

        fun setIngredients(ingredients: MutableList<Ingredient>)
    }
}
