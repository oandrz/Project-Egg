/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import android.net.Uri
import id.zelory.compressor.Compressor
import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.util.ClarifaiHelper
import starbright.com.projectegg.util.Constants
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider
import java.io.File
import java.util.*

internal class IngredientsPresenter(
        private val mRepository: AppRepository,
        private val mView: IngredientsContract.View,
        private val mSchedulerProvider: BaseSchedulerProvider,
        private val mClarifaiHelper: ClarifaiHelper,
        private val compressor: Compressor) : IngredientsContract.Presenter,
        ClarifaiHelper.Callback {

    private val mCompositeDisposable: CompositeDisposable
    private var mCart: MutableList<Ingredient>

    init {
        mView.setPresenter(this)
        mCompositeDisposable = CompositeDisposable()
        mCart = mutableListOf()
    }

    override fun start() {
        mView.setupEtSearchIngredients()
        mView.setupRvIngredientSuggestion()
        if (!mCart.isEmpty()) {
            mView.updateIngredientCount(mCart.size)
        }
    }

    override fun handleActionButtonClicked(query: String) {
        if (query.isEmpty()) {
            mView.openCamera()
        } else {
            mView.clearEtSearchQuery()
        }
    }

    override fun handleCartTvClicked() {
        mView.setupBottomSheetDialogFragment()
        mView.setCartItem(mCart)
        mView.showBottomSheetDialog()
    }

    override fun handleSuggestionTextChanged(query: String) {
        mView.hideSearchSuggestion()
        if (query.isEmpty()) {
            mView.hideSuggestionProgressBar()
            mView.showActionCamera()
        } else {
            mView.hideActionButton()
            mView.showSuggestionProgressBar()
        }
    }

    override fun handleSuggestionItemClicked(ingredient: Ingredient) {
        if (mCart.size == MAX_INGREDIENTS_IN_CART) {
            mView.showItemMaxToast()
        } else if (mCart.contains(ingredient)) {
            mView.showDuplicateItemToast()
        } else {
            mView.showSuccessPutIngredientToast(ingredient.name)
            mCart.add(ingredient)
            mView.updateIngredientCount(mCart.size)
        }
    }

    override fun searchIngredient(query: String) {
        if (query.isEmpty()) {
            return
        }

        mCompositeDisposable
                .add(mRepository.searchIngredient(query)
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe({ ingredients ->
                            mView.hideSuggestionProgressBar()
                            mView.hideSoftKeyboard()
                            if (ingredients.isEmpty()) {
                                mView.showItemEmptyToast()
                            }
                            mView.showActionClear()
                            mView.showSearchSuggestion(ingredients)
                        }, {
                            mView.hideSuggestionProgressBar()
                            mView.showActionClear()
                        })
                )
    }

    override fun setCart(cart: MutableList<Ingredient>) {
        mCart = cart
        mView.updateIngredientCount(cart.size)
    }

    override fun getCart(): ArrayList<Ingredient> {
        return mCart as ArrayList<Ingredient>
    }

    override fun handleCameraResult(filePath: String) {
        mView.setupMaterialProgressDialog()
        mView.showMaterialProgressDialog()
        mClarifaiHelper.predict(Uri.fromFile(compressor.compressToFile(File(filePath))), this)
    }

    override fun onPredictionCompleted(ingredients: String) {
        mView.hideMaterialProgressDialog()

        if (ingredients.isEmpty()) {
            mView.showPredictionEmptyToast()
        } else {
            val ingredientsSplitted = ingredients.split(Constants.COMMA.toRegex())
            ingredientsSplitted.forEach { ingredient ->
                val isIngredientIncluded: Boolean = mCart
                        .asSequence()
                        .map { it.name }
                        .any { it == ingredient }
                if (!isIngredientIncluded) {
                    mCart.add(Ingredient(ingredient))
                }
            }
            mView.updateIngredientCount(mCart.size)
            mView.showItemAddedToast()
        }
    }

    override fun onPredictionFailed() {
        mView.hideMaterialProgressDialog()
        mView.showPredictionEmptyToast()
    }

    companion object {
        private const val MAX_INGREDIENTS_IN_CART = 8
    }
}