/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 18/11/2019.
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
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.response.NetworkError
import starbright.com.projectegg.features.recipelist.RecipeListContract
import starbright.com.projectegg.features.recipelist.RecipeListViewModel
import starbright.com.projectegg.util.*

@RunWith(MockitoJUnitRunner::class)
class RecipeListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockView: RecipeListContract.View

    @Mock
    private lateinit var mockNetworkHelper: NetworkHelper

    @Mock
    private lateinit var mockRepository: AppRepository

    private lateinit var testScheduler: TestScheduler
    private lateinit var viewModel: RecipeListViewModel

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        viewModel = RecipeListViewModel(
            schedulerProvider,
            CompositeDisposable(),
            mockNetworkHelper,
            mockRepository
        )
        viewModel.attachView(mockView)
    }

    @Test
    fun givenAppLaunch_whenOnCreate_shouldSetupView() {
        doReturn(Observable.empty<Recipe>())
            .`when`(mockRepository)
            .getRecipes("", 0)

        viewModel.onCreateScreen()

        verify(mockView).setupView()
    }

    @Test
    fun givenResponse200_whenGetRecipe_shouldRenderView() {
        doReturn(mockIngredients)
            .`when`(mockView)
            .provideSearchConfig()
        doReturn(Observable.just(mockRecipes))
            .`when`(mockRepository)
            .getRecipes(mockIngredient.name, 0)

        viewModel.onCreateScreen()
        testScheduler.triggerActions()

        verify(mockView).hideLoadingBar()
        verify(mockView).bindRecipesToList(mockRecipes.toMutableList())
    }

    @Test
    fun givenResponse400_whenGetRecipe_shouldShowErrorSnackbar() {
        val error = Observable.error<NetworkError>(Exception("s"))

        doReturn(true)
            .`when`(mockNetworkHelper)
            .isConnectedWithNetwork()
        doReturn(mockIngredients)
            .`when`(mockView)
            .provideSearchConfig()
        doReturn(error)
            .`when`(mockRepository)
            .getRecipes(mockIngredient.name, 0)

        viewModel.onCreateScreen()
        testScheduler.triggerActions()

        verify(mockView).hideFooterLoading()
    }
}