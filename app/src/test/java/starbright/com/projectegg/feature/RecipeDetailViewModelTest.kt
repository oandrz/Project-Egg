/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 26/11/2019.
 */

package starbright.com.projectegg.feature

import android.arch.core.executor.testing.InstantTaskExecutorRule
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
import starbright.com.projectegg.features.detail.RecipeDetailViewModel
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.TestSchedulerProvider
import starbright.com.projectegg.util.mockRecipe
import starbright.com.projectegg.util.mockRecipeWithoutUrl

@RunWith(MockitoJUnitRunner::class)
class RecipeDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockView: RecipeDetailContract.View

    @Mock
    private lateinit var mockNetworkHelper: NetworkHelper

    @Mock
    private lateinit var mockRepository: AppRepository

    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        viewModel = RecipeDetailViewModel(
            schedulerProvider,
            CompositeDisposable(),
            mockNetworkHelper,
            mockRepository
        )
        viewModel.attachView(mockView)
    }

    @Test
    fun givenAppLaunch_whenOnCreate_shouldSetupRefreshView() {
        doReturn(Observable.just(mockRecipe))
            .`when`(mockRepository)
            .getRecipeDetailInformation(mockRecipe.id.toString())

        viewModel.onCreateScreen()

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

        viewModel.getRecipeDetailInformation(mockRecipe.id.toString())
        testScheduler.triggerActions()

        verify(mockView).hideScrollContainer()
        verify(mockView).showProgressBar()
        verify(mockView).hideProgressBar()
        verify(mockView).hideEmptyView()
        verify(mockView).renderBannerFoodImage(mockRecipe.image)
        verify(mockView).renderHeaderContainer(
            mockRecipe.servingCount, mockRecipe.cookingMinutes,
            mockRecipe.title,

            )
        mockRecipe.ingredients?.let {
            verify(mockView.renderIngredientsList(it))
        }
        mockRecipe.instructions?.let {
            verify(mockView).renderInstructionsList(it)
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

        viewModel.getRecipeDetailInformation(mockRecipe.id.toString())
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

        viewModel.getRecipeDetailInformation(mockRecipe.id.toString())
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.server_connection_error)
    }

    @Test
    fun givenNoRecipeInformation_whenClickShareAction_shouldShowErrorMessage() {
        viewModel.handleShareMenuClicked()
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

        viewModel.getRecipeDetailInformation(mockRecipe.id.toString())
        viewModel.handleShareMenuClicked()
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

        viewModel.getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())
        viewModel.handleShareMenuClicked()
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

        viewModel.getRecipeDetailInformation(mockRecipeWithoutUrl.id.toString())
        viewModel.handleWebViewMenuClicked()
        testScheduler.triggerActions()

        verify(mockView).showError(R.string.detail_empty_label)
    }
}