package starbright.com.projectegg.features.home.list

import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeHomeContract {

    interface View: BaseViewContract {
        fun setupSearchView()
        fun setupList()
        fun populateList(recipe: List<Recipe>)
        fun showFooterLoading(recipe: List<Recipe>)
        fun showError(error: String)
        fun navigateDetailPage(recipeId: String)
    }

    interface Presenter: BasePresenterContract {
        fun loadRecipe(page: Int)
        fun handleLoadMore(currentPage: Int)
        fun handleItemClick(selectedRecipeId: String)
    }
}