/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 5 - 9 - 2020.
 */

/**
 * Created by Andreas on 26/11/2019.
 */

package starbright.com.projectegg.feature

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import starbright.com.projectegg.data.model.response.NetworkError
import starbright.com.projectegg.features.detail.RecipeDetailContract
import starbright.com.projectegg.features.detail.RecipeDetailPresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.TestSchedulerProvider
import starbright.com.projectegg.util.mockRecipe
import starbright.com.projectegg.util.mockRecipeWithoutUrl

@RunWith(MockitoJUnitRunner::class)
class RecipeDetailPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockView: RecipeDetailContract.View

    @Mock
    private lateinit var mockNetworkHelper: NetworkHelper

    @Mock
    private lateinit var mockRepository: AppRepository

    private lateinit var presenter: RecipeDetailPresenter
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        presenter = RecipeDetailPresenter(
            schedulerProvider,
            CompositeDisposable(),
            mockNetworkHelper,
            mockRepository
        )
        presenter.attachView(mockView)
    }

    @Test
    fun givenAppLaunch_whenOnCreate_shouldSetupRefreshView() {
        doReturn(Observable.just(mockRecipe))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        presenter.onCreateScreen()

        verify(mockView).setupSwipeRefreshLayout()
    }

    @Test
    fun given200SuccessResponse_whenFetchRecipeDetailInformation_shouldRenderView() {
        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        doReturn(Observable.just(mockRecipe))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        presenter.getRecipeDetailInformation(mockRecipe.id.toString())
        testScheduler.triggerActions()

        verify(mockView).hideScrollContainer()
        verify(mockView).showProgressBar()
        verify(mockView).hideProgressBar()
        verify(mockView).hideEmptyView()
        verify(mockView).renderBannerFoodImage(mockRecipe.image.orEmpty())
        verify(mockView).renderHeaderContainer(
            mockRecipe.servingCount ?: 0, mockRecipe.cookingMinutes ?: 0,
            mockRecipe.title, "", 10
        )
        mockRecipe.ingredients?.let {
            verify(mockView.renderIngredientsList(it.toMutableList()))
        }
        mockRecipe.instructions?.let {
            verify(mockView).renderInstructionsList(it.toMutableList())
        }
    }

    @Test
    fun given400ErrorResponse_whenFetchRecipeDetailInformation_shouldRenderView() {
        val error = Observable.error<NetworkError>(Exception("s"))

        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        doReturn(error)
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        presenter.getRecipeDetailInformation(mockRecipe.id.toString())
        testScheduler.triggerActions()

        verify(mockView).renderEmptyView()
    }

    @Test
    fun givenNoNetworkConnection_whenFetchRecipeDetailInformation_shouldShowErrorMessage() {
        val error = Observable.error<NetworkError>(Exception("s"))

        doReturn(false)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        doReturn(error)
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        presenter.getRecipeDetailInformation(mockRecipe.id.toString())
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.server_connection_error)
    }

    @Test
    fun givenNoRecipeInformation_whenClickShareAction_shouldShowErrorMessage() {
        presenter.handleShareMenuClicked()
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.detail_empty_label)
    }

    @Test
    fun givenRecipeInformation_whenClickShareAction_shouldCreateShareIntent() {
        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        doReturn(Observable.just(mockRecipe))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        presenter.getRecipeDetailInformation(mockRecipe.id.toString())
        presenter.handleShareMenuClicked()
        testScheduler.triggerActions()

        mockRecipe.sourceStringUrl?.let {
            verify(mockView).createShareIntent(it, mockRecipe.title)
        }
    }

    @Test
    fun givenRecipeInformationWithoutUrl_whenClickShareAction_shouldShowError() {
        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        mockRecipeWithoutUrl.sourceStringUrl = null
        doReturn(Observable.just(mockRecipeWithoutUrl))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())

        presenter.getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())
        presenter.handleShareMenuClicked()
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.detail_empty_label)
    }

    @Test
    fun givenRecipeInformationWithoutUrl_whenClickWebViewMenu_shouldShowError() {
        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()

        mockRecipeWithoutUrl.sourceStringUrl = null
        doReturn(Observable.just(mockRecipeWithoutUrl))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())

        presenter.getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())
        presenter.handleWebViewMenuClicked()
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.detail_empty_label)
    }
}