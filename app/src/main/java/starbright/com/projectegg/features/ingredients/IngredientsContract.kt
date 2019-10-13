/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.features.base.BasePresenter

internal class IngredientsContract {

    internal interface View {
        fun setupEtSearchIngredients()
        fun setupRvIngredientSuggestion()
        fun setupMaterialProgressDialog()
        fun setupBottomSheetDialogFragment()
        fun setupImageActionButton()
        fun setupTvCartCount()
        fun setupMoreButton()

        fun clearEtSearchQuery()
        fun openCamera()

        fun showSearchSuggestion(ingredients: List<Ingredient>)
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

        fun showBottomSheetDialog()

        fun setCartItem(cart: MutableList<Ingredient>)

        fun updateIngredientCount(count: Int)

        fun hideSoftKeyboard()
    }

    internal interface Presenter : BasePresenter {
        fun handleActionButtonClicked(query: String)
        fun handleCartTvClicked()

        fun handleSuggestionTextChanged(query: String)
        fun handleSuggestionItemClicked(ingredient: Ingredient)
        fun handleCameraResult(filePath: String)

        fun searchIngredient(query: String)
        fun onDestroy()
    }
}