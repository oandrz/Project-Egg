/**
 * Created by Andreas on 5/10/2019.
 */

/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.detail

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction

class RecipeDetailContract {

    interface View {
        fun showProgressBar()
        fun hideProgressBar()

        fun hideScrollContainer()
        fun showScrollContainer()
        fun hideEmptyStateTextView()

        fun renderErrorStateTextView(errorMessage: String)
        fun renderEmptyStateTextView()
        fun renderBannerFoodImage(imageURL: String)

        fun renderHeaderContainer(serving: Int, preparationMinutes: Int, cookingMinutes: Int, recipeName: String)
        fun renderIngredientCard(ingredients: MutableList<Ingredient>)
        fun renderInstructionCard(instructions: MutableList<Instruction>)

        fun setupSwipeRefreshLayout()

        fun createShareIntent(url: String, recipeName: String)

        fun navigateToWebViewActivity(url: String)
    }

    interface Presenter {
        fun setRecipeId(recipeId: String)
        fun getRecipeDetailInformation(recipeId: String)

        fun handleShareMenuClicked()

        fun handleWebViewMenuClicked()
    }
}