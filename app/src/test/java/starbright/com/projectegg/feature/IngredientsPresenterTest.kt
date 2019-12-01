/**
 * Created by Andreas on 17/11/2019.
 */

package starbright.com.projectegg.feature

import android.arch.core.executor.testing.InstantTaskExecutorRule
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.response.NetworkError
import starbright.com.projectegg.features.ingredients.IngredientsContract
import starbright.com.projectegg.features.ingredients.IngredientsPresenter
import starbright.com.projectegg.util.*

@RunWith(MockitoJUnitRunner::class)
class IngredientsPresenterTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockNetworkHelper: NetworkHelper

    @Mock
    private lateinit var mockView: IngredientsContract.View

    @Mock
    private lateinit var compressor: Compressor

    @Mock
    private lateinit var mockRepository: AppRepository

    @Mock
    private lateinit var imageRecognizer: IngredientRecognizer

    private lateinit var presenter: IngredientsPresenter
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        presenter = IngredientsPresenter(
            schedulerProvider,
            CompositeDisposable(),
            mockNetworkHelper,
            compressor,
            mockRepository,
            imageRecognizer
        )
        presenter.attachView(mockView)
    }

    @Test
    fun givenAppLaunch_whenOnCreate_shouldSetupView() {
        presenter.onCreateScreen()
        verify(mockView).setupView()
    }

    @Test
    fun givenEmptyQuery_whenClickActionButton_shouldOpenCamera() {
        presenter.handleActionButtonClicked("")
        verify(mockView).openCamera()
    }

    @Test
    fun givenStringQuery_whenClickActionButton_shouldRenderView() {
        presenter.handleActionButtonClicked("apple")
        verify(mockView).clearEtSearchQuery()
        verify(mockView).showActionCamera()
        verify(mockView).hideSearchSuggestion()
    }

    @Test
    fun givenCart_whenClickCartIcon_showBottomSheetDialog() {
        presenter.handleCartTvClicked()
        verify(mockView).showBottomSheetDialog(mutableListOf())
    }

    @Test
    fun givenStringQuery_whenTextChanged_shouldShowProgressBar() {
        presenter.handleSuggestionTextChanged("appl")
        verify(mockView).hideSearchSuggestion()
        verify(mockView).hideActionButton()
        verify(mockView).showSuggestionProgressBar()
    }

    @Test
    fun givenEmptyQuery_whenTextChanged_shouldShowCameraButton() {
        presenter.handleSuggestionTextChanged("")
        verify(mockView).hideSearchSuggestion()
        verify(mockView).hideSuggestionProgressBar()
        verify(mockView).showActionCamera()
    }

    @Test
    fun givenIngredient_whenCartIsEmptyAndClickSuggestionItem_shouldAddIngredientToCardAndUpdateCartCount() {
        presenter.handleSuggestionItemClicked(mockIngredient)
        verify(mockView).showSuccessPutIngredientToast(mockIngredient.name)
        verify(mockView).updateIngredientCount(1)
    }

    @Test
    fun givenDuplicateIngredient_whenCartIsEmptyAndClickSuggestionItem_shouldShowDuplicateItemToast() {
        presenter.handleSuggestionItemClicked(mockIngredient)
        presenter.handleSuggestionItemClicked(mockIngredient)
        verify(mockView).showDuplicateItemToast()
    }

    @Test
    fun givenIngredientUntilExceedLimit_whenCartIsEmptyAndClickSuggestionItem_shouldShowItemMaxToast() {
        presenter.handleSuggestionItemClicked(Ingredient("banana"))
        presenter.handleSuggestionItemClicked(Ingredient("apple"))
        presenter.handleSuggestionItemClicked(Ingredient("orange"))
        presenter.handleSuggestionItemClicked(Ingredient("ale"))
        presenter.handleSuggestionItemClicked(Ingredient("whisky"))
        presenter.handleSuggestionItemClicked(Ingredient("pork"))
        presenter.handleSuggestionItemClicked(Ingredient("chicken"))
        presenter.handleSuggestionItemClicked(Ingredient("beef"))
        presenter.handleSuggestionItemClicked(Ingredient("lamb"))
        verify(mockView).showItemMaxToast()
    }

    @Test
    fun givenCart_whenClickRemoveItem_shouldUpdateIngredientCount() {
        presenter.handleItemRemovedFromCart()
        verify(mockView).updateIngredientCount(0)
    }

    @Test
    fun givenCart_whenClickSubmitButton_shouldUpdateIngredientCount() {
        presenter.handleSubmitButtonClicked()
        verify(mockView).navigateToRecipeList(listOf())
    }

    @Test
    fun givenNoInternetConnection_whenSearchingIngredient_shouldShowErrorConnectionToast() {
        doReturn(false).`when`(mockNetworkHelper).isConnectedWithNetwork()
        presenter.searchIngredient("tes")
        verify(mockView).showError(R.string.server_connection_error)
    }

    @Test
    fun givenStringQueryAndResponse200_whenSearchingIngredient_shouldRenderView() {
        val query = "apple"
        doReturn(true).`when`(mockNetworkHelper).isConnectedWithNetwork()
        doReturn(Observable.just(mockIngredients)).`when`(mockRepository).searchIngredient(query)
        presenter.searchIngredient(query)
        testScheduler.triggerActions()
        verify(mockView).hideSuggestionProgressBar()
        verify(mockView).hideSoftKeyboard()
        verify(mockView).showActionClear()
        verify(mockView).updateSuggestion(mockIngredients)
    }

    @Test
    fun givenUnknownIngredientQueryAndResponse200_whenSearchingIngredient_shouldRenderView() {
        val query = "bandage"
        doReturn(true).`when`(mockNetworkHelper).isConnectedWithNetwork()
        doReturn(Observable.just(listOf<Ingredient>())).`when`(mockRepository).searchIngredient(query)
        presenter.searchIngredient(query)
        testScheduler.triggerActions()
        verify(mockView).hideSuggestionProgressBar()
        verify(mockView).hideSoftKeyboard()
        verify(mockView).showActionClear()
        verify(mockView).showItemEmptyToast()
        verify(mockView).updateSuggestion(listOf())
    }

    @Test
    fun givenErrorResponse_whenSearchingIngredient_shouldRenderView() {
        val query = "apple"
        val error = Observable.error<NetworkError>(Exception("s"))
        doReturn(true).`when`(mockNetworkHelper).isConnectedWithNetwork()
        doReturn(error).`when`(mockRepository).searchIngredient(query)
        presenter.searchIngredient(query)
        testScheduler.triggerActions()
        verify(mockView).hideSearchSuggestion()
        verify(mockView).hideSuggestionProgressBar()
    }
}