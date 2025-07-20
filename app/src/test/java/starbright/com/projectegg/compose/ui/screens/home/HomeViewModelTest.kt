package starbright.com.projectegg.compose.ui.screens.home

import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import starbright.com.projectegg.data.RecipeRepository
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.response.RecipeListResponse
import starbright.com.projectegg.util.scheduler.TestSchedulerProvider

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = androidx.arch.core.InstantTaskExecutorRule()

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    private lateinit var schedulerProvider: TestSchedulerProvider
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        schedulerProvider = TestSchedulerProvider()
        viewModel = HomeViewModel(recipeRepository, schedulerProvider)
    }

    @Test
    fun `loadRecipes should update state with recipes on success`() = runTest {
        // Given
        val mockRecipes = listOf(
            Recipe(
                id = "1",
                title = "Pasta Carbonara",
                image = "pasta.jpg",
                readyInMinutes = 30,
                servings = 4,
                sourceName = "Italian Kitchen"
            ),
            Recipe(
                id = "2", 
                title = "Caesar Salad",
                image = "salad.jpg",
                readyInMinutes = 15,
                servings = 2,
                sourceName = "Healthy Eats"
            )
        )
        val mockResponse = RecipeListResponse(results = mockRecipes)
        
        `when`(recipeRepository.getRandomRecipe(0))
            .thenReturn(Single.just(mockResponse))

        // When
        viewModel.loadRecipes()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after success" }
        assert(state.error == null) { "Should have no error" }
        assert(state.recipes == mockRecipes) { "Should have loaded recipes" }
    }

    @Test
    fun `loadRecipes should update state with error on failure`() = runTest {
        // Given
        val error = Exception("Network error")
        `when`(recipeRepository.getRandomRecipe(0))
            .thenReturn(Single.error(error))

        // When
        viewModel.loadRecipes()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(!state.isLoading) { "Should not be loading after error" }
        assert(state.error == "Network error") { "Should have error message" }
        assert(state.recipes.isEmpty()) { "Should have no recipes on error" }
    }

    @Test
    fun `loadMoreRecipes should append recipes to existing list`() = runTest {
        // Given
        val initialRecipes = listOf(
            Recipe(id = "1", title = "Recipe 1", image = "", readyInMinutes = 30, servings = 4, sourceName = "")
        )
        val moreRecipes = listOf(
            Recipe(id = "2", title = "Recipe 2", image = "", readyInMinutes = 20, servings = 2, sourceName = "")
        )
        
        `when`(recipeRepository.getRandomRecipe(0))
            .thenReturn(Single.just(RecipeListResponse(results = initialRecipes)))
        `when`(recipeRepository.getRandomRecipe(1))
            .thenReturn(Single.just(RecipeListResponse(results = moreRecipes)))

        // When
        viewModel.loadRecipes()
        schedulerProvider.triggerActions()
        viewModel.loadMoreRecipes()
        schedulerProvider.triggerActions()

        // Then
        val state = viewModel.uiState.value
        assert(state.recipes.size == 2) { "Should have both recipes" }
        assert(state.recipes[0].id == "1") { "Should maintain order" }
        assert(state.recipes[1].id == "2") { "Should append new recipes" }
    }

    @Test
    fun `onSearchClicked should trigger navigation event`() = runTest {
        // When
        viewModel.onSearchClicked()

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is HomeNavigationEvent.NavigateToSearch) { 
            "Should emit NavigateToSearch event" 
        }
    }

    @Test
    fun `onRecipeClicked should trigger navigation event with recipe id`() = runTest {
        // Given
        val recipeId = "123"

        // When
        viewModel.onRecipeClicked(recipeId)

        // Then
        val event = viewModel.navigationEvent.value
        assert(event is HomeNavigationEvent.NavigateToRecipeDetail) { 
            "Should emit NavigateToRecipeDetail event" 
        }
        assert((event as HomeNavigationEvent.NavigateToRecipeDetail).recipeId == recipeId) {
            "Should pass correct recipe id"
        }
    }
} 