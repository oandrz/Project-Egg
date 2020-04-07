/**
 * Created by Andreas on 9/9/2018.
 */

package starbright.com.projectegg.features.ingredients

import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.Constants
import starbright.com.projectegg.util.IngredientRecognizer
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class IngredientsPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val compressor: Compressor,
    private val repository: AppRepository,
    private val ingredientRecognizer: IngredientRecognizer
) : BasePresenter<IngredientsContract.View>(schedulerProvider, compositeDisposable, networkHelper),
    IngredientsContract.Presenter {

    private var counter: Int = 0

    private var cart: MutableList<Ingredient> = mutableListOf()
        set(value) {
            field = value
            view.updateIngredientCount(value.size)
        }

    override fun onCreateScreen() {
        view.run {
            setupView()
            if (cart.isNotEmpty()) updateIngredientCount(cart.size)
        }
    }

    override fun handleActionButtonClicked(query: String) {
        if (query.isNotEmpty()) {
            view.run {
                clearEtSearchQuery()
                showActionCamera()
                hideSearchSuggestion()
            }
        } else {
            view.openCamera()
        }
    }

    override fun handleCartTvClicked() {
        view.showBottomSheetDialog(cart)
    }

    override fun handleSuggestionTextChanged(query: String) {
        view.run {
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
        view.run {
            when {
                cart.size >= MAX_INGREDIENTS_IN_CART -> showItemMaxToast()
                cart.contains(ingredient) -> showDuplicateItemToast()
                else -> {
                    showSuccessPutIngredientToast(ingredient.name)
                    cart.add(ingredient)
                    updateIngredientCount(cart.size)
                }
            }
        }
    }

    override fun handleItemRemovedFromCart() {
        view.updateIngredientCount(cart.size)
    }

    override fun handleTvTitleClicked() {
        counter++
        if (counter >= 5) {
            view.navigateToIncomePage()
        }
    }

    override fun handleSubmitButtonClicked() {
        view.navigateToRecipeList(cart)
    }

    override fun searchIngredient(query: String) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)
        else if (query.isEmpty() && compositeDisposable.size() > 0) compositeDisposable.clear()

        compositeDisposable.add(
            Observable.just(query)
                .debounce(30, TimeUnit.SECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .switchMap { repository.searchIngredient(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ ingredients ->
                    view.run {
                        hideSuggestionProgressBar()
                        if (ingredients.isEmpty()) {
                            showItemEmptyToast()
                        }
                        showActionClear()
                        updateSuggestion(ingredients)
                    }
                }, {
                    view.run {
                        hideSuggestionProgressBar()
                        hideSearchSuggestion()
                        handleNetworkError(it)
                    }
                })
        )
    }

    override fun handleCameraResult(filePath: String) {
        view.showMaterialProgressDialog()
        ingredientRecognizer.predict(compressor.compressToFile(File(filePath)), {
            view.run {
                hideMaterialProgressDialog()
                if (it.isEmpty()) {
                    showPredictionEmptyToast()
                } else {
                    it.split(Constants.COMMA.toRegex()).forEach { ingredient ->
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
        }, {
            view.run {
                hideMaterialProgressDialog()
                showPredictionEmptyToast()
            }
        })
    }

    companion object {
        private const val MAX_INGREDIENTS_IN_CART = 8
    }
}