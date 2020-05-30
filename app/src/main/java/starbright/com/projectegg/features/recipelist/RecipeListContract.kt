/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeListContract {

    interface View : BaseViewContract {
        fun setupView()
        fun provideSearchConfig(): RecipeConfig
        fun showFooter()
        fun appendRecipes(recipes: List<Recipe>)
        fun showDetail(recipeId: String)
        fun showFilterBottomSheet(
            cuisines: List<String>,
            selectedCuisine: String?
        )

        fun hideFooterLoading()
    }

    interface Presenter : BasePresenterContract {
        fun handleListItemClicked(selectedRecipeId: String)
        fun handleRefresh()
        fun handleLoadMore(lastPosition: Int)
        fun handleFilterActionClicked()
        fun handleFilterItemSelected(cuisine: String)
    }
}
