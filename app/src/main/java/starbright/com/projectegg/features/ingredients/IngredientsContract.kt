/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class IngredientsContract {

    interface View : BaseViewContract {
        fun setupView()

        fun clearEtSearchQuery()
        fun openCamera()

        fun updateSuggestion(ingredients: List<Ingredient>)
        fun hideSearchSuggestion()

        fun showActionCamera()
        fun showActionClear()
        fun hideActionButton()

        fun showSuggestionProgressBar()
        fun hideSuggestionProgressBar()

        fun showItemEmptyToast()
        fun showItemMaxToast()
        fun showDuplicateItemToast()
        fun showPredictionEmptyToast()

        fun showItemAddedToast()

        fun showSuccessPutIngredientToast(ingredientName: String)

        fun showMaterialProgressDialog()
        fun hideMaterialProgressDialog()

        fun showBottomSheetDialog(cart: MutableList<Ingredient>)

        fun updateIngredientCount(count: Int)

        fun hideSoftKeyboard()

        fun navigateToRecipeList(cart: List<Ingredient>)
    }

    interface Presenter : BasePresenterContract {
        fun handleActionButtonClicked(query: String)
        fun handleCartTvClicked()

        fun handleSuggestionTextChanged(query: String)
        fun handleSuggestionItemClicked(ingredient: Ingredient)
        fun handleCameraResult(filePath: String)

        fun handleSubmitButtonClicked()
        fun handleItemRemovedFromCart()

        fun searchIngredient(query: String)
    }
}