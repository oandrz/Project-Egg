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
import javax.inject.Inject

class IngredientsPresenter @Inject constructor(
        private val repository: AppRepository,
        private val schedulerProvider: BaseSchedulerProvider,
        private val clarifaiHelper: ClarifaiHelper,
        private val compressor: Compressor,
        private val compositeDiposable: CompositeDisposable
) : IngredientsContract.Presenter, ClarifaiHelper.Callback {

    internal var cart: MutableList<Ingredient> = mutableListOf()
    set(value) {
        field = value
        view?.updateIngredientCount(value.size)
    }
    internal var view: IngredientsContract.View? = null

    override fun start() {
        view?.run {
            setupEtSearchIngredients()
            setupRvIngredientSuggestion()
            setupTvCartCount()
            setupMoreButton()
            setupImageActionButton()
            if (cart.isNotEmpty()) updateIngredientCount(cart.size)
        }
    }

    override fun handleActionButtonClicked(query: String) {
        view?.run {
            openCamera()
            clearEtSearchQuery()
        }
    }

    override fun handleCartTvClicked() {
        view?.run {
            setupBottomSheetDialogFragment()
            setCartItem(cart)
            showBottomSheetDialog()
        }
    }

    override fun handleSuggestionTextChanged(query: String) {
        view?.run {
            hideSearchSuggestion()
            if (query.isEmpty()) {
                hideSuggestionProgressBar()
                showActionCamera()
            } else {
                hideActionButton()
                showSuggestionProgressBar()
            }
        }
    }

    override fun handleSuggestionItemClicked(ingredient: Ingredient) {
        view?.run {
            when {
                cart.size == MAX_INGREDIENTS_IN_CART -> showItemMaxToast()
                cart.contains(ingredient) -> showDuplicateItemToast()
                else -> {
                    showSuccessPutIngredientToast(ingredient.name)
                    cart.add(ingredient)
                    updateIngredientCount(cart.size)
                }
            }
        }
    }

    override fun searchIngredient(query: String) {
        if (query.isEmpty()) return

        compositeDiposable.add(
            repository
                    .searchIngredient(query)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ ingredients ->
                        view?.run {
                            hideSuggestionProgressBar()
                            hideSoftKeyboard()
                            if (ingredients.isEmpty()) {
                                showItemEmptyToast()
                            }
                            showActionClear()
                            showSearchSuggestion(ingredients)
                        }
                    }, {
                        view?.run {
                            hideSuggestionProgressBar()
                            showActionClear()
                        }
                    })
        )
    }

    override fun handleCameraResult(filePath: String) {
        view?.run {
            setupMaterialProgressDialog()
            showMaterialProgressDialog()
        }
        clarifaiHelper.predict(Uri.fromFile(compressor.compressToFile(File(filePath))), this)
    }

    override fun onPredictionCompleted(ingredients: String) {
        view?.run {
            hideMaterialProgressDialog()
            if (ingredients.isEmpty()) {
                showPredictionEmptyToast()
            } else {
                ingredients.split(Constants.COMMA.toRegex()).forEach { ingredient ->
                    val isIngredientIncluded: Boolean = cart.asSequence()
                            .map { it.name }
                            .any { it == ingredient }
                    if (!isIngredientIncluded) {
                        cart.add(Ingredient(ingredient))
                    }
                }
                updateIngredientCount(cart.size)
                showItemAddedToast()
            }
        }
    }

    override fun onPredictionFailed() {
        view?.run {
            hideMaterialProgressDialog()
            showPredictionEmptyToast()
        }
    }

    override fun onDestroy() {
        compositeDiposable.clear()
    }

    companion object {
        private const val MAX_INGREDIENTS_IN_CART = 8
    }
}